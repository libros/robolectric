package org.robolectric.util;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.robolectric.util.SQLite.*;

@RunWith(TestRunners.WithDefaults.class)
public class SQLite_Test {
    ContentValues values;
    @Before
    public void setUp() throws Exception {
        String byteString = "byte_string";
        byte[] byteData = byteString.getBytes();

        values = new ContentValues();
        values.put("name", "Chuck");
        values.put("int_value", 33);
        values.put("float_value", (float) 1.5);
        values.put("byte_data", byteData);
    }

    @Test
    public void testBuildInsertString() throws Exception {
        SQLite.SQLStringAndBindings insertString = buildInsertString("table_name", values, SQLiteDatabase.CONFLICT_NONE);
        assertThat(insertString.sql).isEqualTo("INSERT INTO table_name (float_value, byte_data, name, int_value) VALUES (?, ?, ?, ?);");
        SQLiteTestHelper.verifyColumnValues(insertString.columnValues);
    }

    @Test
    public void testBuildUpdateString() {
        SQLite.SQLStringAndBindings insertString = buildUpdateString("table_name", values, "id=?", new String[]{"1234"});
        assertThat(insertString.sql).isEqualTo("UPDATE table_name SET float_value=?, byte_data=?, name=?, int_value=? WHERE id='1234';");
        SQLiteTestHelper.verifyColumnValues(insertString.columnValues);
    }

    @Test
    public void testBuildDeleteString() {
        String deleteString = buildDeleteString("table_name", "id=?", new String[]{"1234"});
        assertThat(deleteString).isEqualTo("DELETE FROM table_name WHERE id='1234';");
    }

    @Test
    public void testBuildWhereClause() {
        String whereClause = buildWhereClause("id=? AND name=? AND int_value=?", new String[]{"1234", "Chuck", "33"});
        assertThat(whereClause).isEqualTo("id='1234' AND name='Chuck' AND int_value='33'");
    }

    @Test
    public void testBuildColumnValuesClause() {
        SQLStringAndBindings columnValuesClause = buildColumnValuesClause(values);

        assertThat(columnValuesClause.sql).isEqualTo("(float_value, byte_data, name, int_value) VALUES (?, ?, ?, ?)");
        SQLiteTestHelper.verifyColumnValues(columnValuesClause.columnValues);
    }

    @Test
    public void testBuildColumnAssignmentsClause() {
        SQLStringAndBindings columnAssignmentsClause = buildColumnAssignmentsClause(values);

        assertThat(columnAssignmentsClause.sql).isEqualTo("float_value=?, byte_data=?, name=?, int_value=?");
        SQLiteTestHelper.verifyColumnValues(columnAssignmentsClause.columnValues);
    }
}
