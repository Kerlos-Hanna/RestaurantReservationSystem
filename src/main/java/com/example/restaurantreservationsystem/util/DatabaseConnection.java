using System;
using System.Data;
using System.IO;
using Microsoft.Data.SqlClient;

public class DatabaseConnection
{
    private static DatabaseConnection _instance;
    private static readonly object _lock = new object();
    private SqlConnection _connection;

    private const string ConnectionString =
        "Data Source=.;Initial Catalog=NORTHWIND;Integrated Security=True;Trust Server Certificate=True";

    private DatabaseConnection() { }

    public static DatabaseConnection GetInstance()
    {
        if (_instance == null)
        {
            lock (_lock)
            {
                if (_instance == null)
                    _instance = new DatabaseConnection();
            }
        }
        return _instance;
    }

    public IDbConnection GetConnection()
    {
        if (_connection == null)
            _connection = new SqlConnection(ConnectionString);

        if (_connection.State == ConnectionState.Closed ||
            _connection.State == ConnectionState.Broken)
            _connection.Open();

        return _connection;
    }

    public void InitializeDatabase()
    {
        string sqlFilePath = "restaurant_reservation.sql";

        if (!File.Exists(sqlFilePath))
            throw new FileNotFoundException($"SQL script not found: {sqlFilePath}");

        string sqlScript = File.ReadAllText(sqlFilePath);

        string[] batches = sqlScript.Split(
            new[] { "\nGO", "\r\nGO", " GO" },
            StringSplitOptions.RemoveEmptyEntries
        );

        var conn = GetConnection();

        foreach (string batch in batches)
        {
            string trimmed = batch.Trim();
            if (string.IsNullOrWhiteSpace(trimmed)) continue;

            using var command = conn.CreateCommand();
            command.CommandText = trimmed;
            command.ExecuteNonQuery();
        }
    }

    public void CloseConnection()
    {
        if (_connection != null)
        {
            if (_connection.State != ConnectionState.Closed)
                _connection.Close();

            _connection.Dispose();
            _connection = null;
        }
    }
}
