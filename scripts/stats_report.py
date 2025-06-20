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

    alquiler_query = (
        "SELECT YEAR(fecha_inicio_efectivo) AS anio, MONTH(fecha_inicio_efectivo) AS mes, "
        "COUNT(*) AS total, SUM(coste_total) AS ingresos FROM alquiler "
        "GROUP BY anio, mes ORDER BY anio, mes"
    )
    reserva_query = (
        "SELECT YEAR(fecha_inicio) AS anio, MONTH(fecha_inicio) AS mes, COUNT(*) AS total "
        "FROM reserva GROUP BY anio, mes ORDER BY anio, mes"
    )

    alquiler_stats = fetch_stats(conn, alquiler_query)
    reserva_stats = fetch_stats(conn, reserva_query)
    conn.close()

    df_alquiler = pd.DataFrame(alquiler_stats, columns=["A\u00f1o", "Mes", "Alquileres", "Ingresos"])
    df_reserva = pd.DataFrame(reserva_stats, columns=["A\u00f1o", "Mes", "Reservas"])

    print("Alquileres por mes:\n", df_alquiler)
    print("\nReservas por mes:\n", df_reserva)

    fig, ax1 = plt.subplots(figsize=(10,4))
    ax1.plot(pd.to_datetime(df_alquiler[["A\u00f1o","Mes"]].assign(day=1)), df_alquiler["Alquileres"], marker='o', label='Alquileres')
    ax1.plot(pd.to_datetime(df_reserva[["A\u00f1o","Mes"]].assign(day=1)), df_reserva["Reservas"], marker='o', label='Reservas')
    ax1.set_xlabel('Mes')
    ax1.set_ylabel('Cantidad')
    ax1.set_title('Evoluci\u00f3n mensual')
    ax1.legend()
    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    main()
