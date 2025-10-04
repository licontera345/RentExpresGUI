import configparser
import mysql.connector
import pandas as pd
import matplotlib.pyplot as plt
import os


def load_config(path="config/config.properties"):
    config = configparser.ConfigParser(strict=False)
    if not os.path.isabs(path):
        # Allow running from repo root or scripts directory
        base = os.path.dirname(os.path.abspath(__file__))
        path = os.path.join(base, os.pardir, path)
    with open(path) as f:
        config.read_string("[DEFAULT]\n" + f.read())
    db_config = {
        "host": config["DEFAULT"].get("db.url").split("//")[-1].split(":")[0],
        "port": int(config["DEFAULT"].get("db.url").split(":")[-1].split("/")[0]),
        "database": config["DEFAULT"].get("db.url").split("/")[-1].split("?")[0],
        "user": config["DEFAULT"].get("db.user"),
        "password": config["DEFAULT"].get("db.password"),
    }
    return db_config


def fetch_stats(conn, query):
    cur = conn.cursor()
    cur.execute(query)
    rows = cur.fetchall()
    cur.close()
    return rows


def main():
    db_config = load_config()
    conn = mysql.connector.connect(**db_config)

    rental_query = (
        "SELECT YEAR(actual_start_date) AS year, MONTH(actual_start_date) AS month, "
        "COUNT(*) AS total, SUM(total_cost) AS revenue FROM rental "
        "GROUP BY year, month ORDER BY year, month"
    )
    reservation_query = (
        "SELECT YEAR(start_date) AS year, MONTH(start_date) AS month, COUNT(*) AS total "
        "FROM reservation GROUP BY year, month ORDER BY year, month"
    )

    rental_stats = fetch_stats(conn, rental_query)
    reservation_stats = fetch_stats(conn, reservation_query)
    conn.close()

    df_rental = pd.DataFrame(rental_stats, columns=["Year", "Month", "Rentals", "Revenue"])
    df_reservation = pd.DataFrame(reservation_stats, columns=["Year", "Month", "Reservations"])

    print("Rentals per month:\n", df_rental)
    print("\nReservations per month:\n", df_reservation)

    fig, ax1 = plt.subplots(figsize=(10, 4))
    ax1.plot(
        pd.to_datetime(df_rental[["Year", "Month"]].assign(day=1)),
        df_rental["Rentals"],
        marker="o",
        label="Rentals",
    )
    ax1.plot(
        pd.to_datetime(df_reservation[["Year", "Month"]].assign(day=1)),
        df_reservation["Reservations"],
        marker="o",
        label="Reservations",
    )
    ax1.set_xlabel("Month")
    ax1.set_ylabel("Count")
    ax1.set_title("Monthly evolution")
    ax1.legend()
    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    main()
