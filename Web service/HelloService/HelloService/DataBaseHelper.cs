using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Npgsql;
using Mono.Security;
using System.Data;

namespace HelloService
{
    public class DataBaseHelper
    {

        public static string ConnectionString = "HOST=localhost;PORT=5432;PROTOCOL=3;DATABASE=Positioning;USER ID=postgres;PASSWORD=1manoilov6;";

        public static DataTable GetData(string sqlQuery)
        {
            NpgsqlConnection sqlConnection = new NpgsqlConnection(ConnectionString);
            NpgsqlCommand sqlCmd = new NpgsqlCommand();
            sqlCmd.CommandText = sqlQuery;
            sqlCmd.Connection = sqlConnection;
            DataSet ds = new DataSet("Random");

            try
            {
                sqlConnection.Open();
                NpgsqlDataAdapter sqlAdapter = new NpgsqlDataAdapter();
                sqlAdapter.SelectCommand = sqlCmd;
                sqlAdapter.Fill(ds);
                return ds.Tables[0];
            }
            catch (NpgsqlException sqlEx)
            {
                //MessageBox.Show(sqlEx.Message, "Erorr", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
            finally
            {
                if (sqlConnection.State == ConnectionState.Open)
                {
                    sqlConnection.Close();
                }
            }
        }
    }
}