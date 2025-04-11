package dao;

import java.util.List;

import Entity.Tax;


import exception.TaxCalculationException;



public interface ITaxService {
    double calculateTax(int employeeId, int taxYear);
    List<Tax> getAllTaxes();  //  Required for report
}
