package com.pinguela.rentexpres.desktop.controller;

import java.awt.Frame;

import com.pinguela.rentexpres.desktop.view.AbstractSearchView;

public abstract class AbstractSearchController<DTO, CRIT, VIEW extends AbstractSearchView<?, ?, ?>> {

    protected final VIEW view;
    protected final Frame frame;
    protected int currentPage = 1;
    protected int totalPages = 1;
    private final int pageSize;

    public AbstractSearchController(VIEW view, Frame frame, int pageSize) {
        this.view = view;
        this.frame = frame;
        this.pageSize = pageSize;
    }

    protected abstract void buscar(CRIT criteria);
    protected abstract CRIT buildCriteria();

    public void goFirstPage() {
        currentPage = 1;
        buscar(buildCriteria());
    }

    public void nextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            buscar(buildCriteria());
        }
    }

    public void prevPage() {
        if (currentPage > 1) {
            currentPage--;
            buscar(buildCriteria());
        }
    }

    protected void updatePagination(int totalRecords) {
        this.totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        view.getPager().setInfo(currentPage, totalPages);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
} 
