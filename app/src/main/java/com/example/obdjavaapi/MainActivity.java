package com.example.obdjavaapi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obdjavaapi.ObdAdapter;
import com.github.pires.obd.commands.SpeedCommand;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference the TextView
        TextView speedText = findViewById(R.id.speed_text);

        new Thread(() -> {
            // Create an instance of ObdAdapter
            ObdAdapter obdAdapter = new ObdAdapter();

            // Call connectToObdAdapter
            Socket socket = obdAdapter.connectToObdAdapter();
            if (socket != null) {
                runOnUiThread(() -> speedText.setText("Connection successful!"));
                try {
                    // Initialize OBD-II adapter
                    new com.github.pires.obd.commands.protocol.EchoOffCommand()
                            .run(socket.getInputStream(), socket.getOutputStream());
                    new com.github.pires.obd.commands.protocol.LineFeedOffCommand()
                            .run(socket.getInputStream(), socket.getOutputStream());
                    new com.github.pires.obd.commands.protocol.SelectProtocolCommand(
                            com.github.pires.obd.enums.ObdProtocols.AUTO)
                            .run(socket.getInputStream(), socket.getOutputStream());


                    while (true) {
                        try {
                            SpeedCommand speedCommand = new SpeedCommand();
                            speedCommand.run(socket.getInputStream(), socket.getOutputStream());
                            String speed = "Vehicle Speed: " + speedCommand.getMetricSpeed() + " km/h";

                            // Update the TextView on the UI thread
                            runOnUiThread(() -> speedText.setText(speed));

                            Thread.sleep(1000); // Fetch speed every second
                        } catch (Exception e) {
                            e.printStackTrace();
                            break; // Exit the loop if an error occurs
                        }
                    }


                    // Fetch and display vehicle speed
                    SpeedCommand speedCommand = new SpeedCommand();
                    speedCommand.run(socket.getInputStream(), socket.getOutputStream());
                    String speed = "Vehicle Speed: " + speedCommand.getMetricSpeed() + " km/h";

                    // Update the TextView on the UI thread
                    runOnUiThread(() -> speedText.setText(speed));
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> speedText.setText("Error: " + e.getMessage()));
                }
            } else {
                runOnUiThread(() -> speedText.setText("Connection failed!"));
            }
        }).start();
    }
}
