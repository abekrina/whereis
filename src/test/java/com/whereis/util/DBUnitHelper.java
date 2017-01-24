package com.whereis.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;

public class DBUnitHelper {

    final static Logger logger = LogManager.getLogger(DBUnitHelper.class);

    private static IDatabaseTester databaseTester = null;
    private static IDataSet dataSet = null;

    /**
     * @param fileName The name of the file (full path actually) storing flat dataset information for the test
     * @return DBUnit IDataSet for the given file
     * @throws MalformedURLException
     * @throws DataSetException
     */
    public static IDataSet readDataSet(String fileName) throws MalformedURLException, DataSetException {
        File file = new File(fileName);
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        dataSet = builder.build(file);
        return dataSet;
    }


    /**
     * Sets up working IDatabaseTester for the current test Suite
     * @param dataSource data source to connect to DB. For now i just use data sources provided by Spring injections
     * @param dataSet data for testing
     * @return IDatabaseTester
     * @throws Exception
     */
    public static IDatabaseTester setUpDatabaseTester(DataSource dataSource, IDataSet dataSet) throws Exception {

        databaseTester = new DataSourceDatabaseTester(dataSource);

        //Set up before and after test behaviour
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.TRUNCATE_TABLE);
        //Applying dataset
        databaseTester.setDataSet(dataSet);

        return databaseTester;
    }
}