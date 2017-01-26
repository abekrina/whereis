package com.whereis.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.test.context.transaction.TestTransaction;

import java.io.File;
import java.net.MalformedURLException;

public class DBUnitHelper {

    final static Logger logger = LogManager.getLogger(DBUnitHelper.class);

    public static final String BASE = "src/test/resources/";

    /**
     * @param fileName The name of the file (full path actually) storing flat dataset information for the test
     * @return DBUnit IDataSet for the given file
     * @throws MalformedURLException
     * @throws DataSetException
     */
    public static IDataSet readDataSet(String fileName) throws MalformedURLException, DataSetException {
        File file = new File(BASE + fileName);
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(file);
        return dataSet;
    }


    /**
     * Apply another dataset to IDatabaseTester
     * @param databaseTester IDatabaseTester which needs to be configured
     * @param dataSetPath  path to file with data for testing
     * @return IDatabaseTester
     * @throws Exception
     */
    public static IDatabaseTester applyDataset(IDatabaseTester databaseTester, String dataSetPath) throws Exception {
        IDataSet dataSet = readDataSet(dataSetPath);
        databaseTester.setDataSet(dataSet);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.onSetup();
        return databaseTester;
    }
}