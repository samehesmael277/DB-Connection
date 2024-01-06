package com.example.db;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.db.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

//    private static final String URL = "jdbc:sybase:Tds:your_sybase_server_ip:port/your_database_name";

    private String sybase_server_id;
    private String port;
    private String database_name;
    private String username;
    private String password;
    private String query;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnConnect.setOnClickListener(v -> validateData());
    }

    private void validateData() {
        sybase_server_id = binding.etSybaseServerId.getText().toString();
        port = binding.etPort.getText().toString();
        database_name = binding.etDatabaseName.getText().toString();
        username = binding.etUsername.getText().toString();
        password = binding.etPassword.getText().toString();
        query = binding.etQuery.getText().toString();
        if (
                sybase_server_id.isEmpty() ||
                port.isEmpty() ||
                database_name.isEmpty() ||
                username.isEmpty() ||
                password.isEmpty() ||
                query.isEmpty()
        ) {
            Log.d("MYAPPTAG", "validateData: " + sybase_server_id.isEmpty() +
                    port.isEmpty() +
                    database_name.isEmpty() +
                    username.isEmpty() +
                    password.isEmpty() +
                    query.isEmpty());
            updateResultMessage("Invalid Data");
            return;
        }

        url = "jdbc:sybase:Tds:" + sybase_server_id + ":" + port +"/" + database_name;

        try {
            new DatabaseTask().execute();
        } catch (Exception e) {
            updateResultMessage(e.getMessage());
        }
    }

    private void updateResultMessage(String value) {
        binding.tvResult.setText(value);
    }

    private class DatabaseTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder result = new StringBuilder();

            try {
                Class.forName("com.sybase.jdbc4.jdbc.SybDriver");
                Connection connection = DriverManager.getConnection(url, username, password);

                // Retrieve table names from a specific database
                PreparedStatement statement = connection.prepareStatement(query);

                ResultSet resultSet = statement.executeQuery();
                Log.d("MYAPPTAG", "doInBackground: " + resultSet);

//                while (resultSet.next()) {
//                    String tableName = resultSet.getString("table_name");
//                    result.append("Table: ").append(tableName).append("\n");
//
//                    // Perform CRUD operations here if needed
//                    // Example: Insert data
//                    // PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " (column1, column2) VALUES (?, ?)");
//                    // insertStatement.setString(1, "value1");
//                    // insertStatement.setString(2, "value2");
//                    // insertStatement.executeUpdate();
//                }

                // Close connections
                resultSet.close();
                statement.close();
                connection.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                result.append("Error: ").append(e.getMessage());
                Log.d("MYAPPTAG", "doInBackground: " + result);
            }

            Log.d("MYAPPTAG", "doInBackground: " + result);
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("MYAPPTAG", "doInBackground: " + result);
        }
    }
}

