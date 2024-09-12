package com.example.setdateandtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.IOException;
public class ResultAcitivity extends AppCompatActivity {

    EditText edittext1;
    TextView currentTimeTextView;
    String newDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_acitivity);

        edittext1 = findViewById(R.id.edittext1);
        String uriString = getIntent().getStringExtra("uri");
        Uri uri = Uri.parse(uriString);
        _extractTimeFromUri(getApplicationContext(), uri);

        Button setDate = findViewById(R.id.SetToSystemTime);
        currentTimeTextView = findViewById(R.id.currentTime);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Command to set system date and time
                    String command = newDate; // "date 021104042002.00";

                    // Run the command with su and sh to ensure correct execution environment
                    Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "sh", "-c", command});

                    // Capture output from the process
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }

                    // Capture error output from the process
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    StringBuilder errorOutput = new StringBuilder();
                    while ((line = errorReader.readLine()) != null) {
                        errorOutput.append(line).append("\n");
                    }

                    // Wait for the command to execute and get the exit code
                    int exitCode = process.waitFor();

                    // Log the command output and error output
                    String currentTime = getCurrentSystemTime();
                    currentTimeTextView.setText("Current Time: " + currentTime);
                    Toast.makeText(ResultAcitivity.this, "Command Output: " + output.toString() + "\nError Output: " + errorOutput.toString() + "\nExit Code: " + exitCode, Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private String getCurrentSystemTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


    public void _extractTimeFromUri(Context context, Uri _uri) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        try {
            InputImage image = InputImage.fromFilePath(context, _uri);
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    String recognizedText = visionText.getText();
                                    String cleanedText = cleanText(recognizedText);  // Clean the text
                                    String dateTime = extractDateTime(cleanedText);  // Extract date and time
                                    newDate = dateTime;
                                    if (dateTime != null) {
                                        edittext1.setText(dateTime);
                                    } else {
                                        edittext1.setText("Date and time not found : " + recognizedText);
                                    }
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace(); // Log the error

                                        }
                                    }
                            );
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
    private String cleanText(String text) {
        // Replace non-numeric characters like commas, periods, etc., with spaces, except for necessary date-time separators
        return text.replaceAll("[,;:.]", " ").trim();
    }
    private String extractDateTime(String text) {
        // Regular expression to match a flexible date-time pattern
        String regex = "(\\d{4})\\s*(\\d{1,2})\\s*(\\d{1,2})\\s*(\\d{1,2})\\s*(\\d{1,2})\\s*(\\d{1,2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            // Extract year, month, day, hour, minute, second
            String year = matcher.group(1);
            String month = matcher.group(2);
            String day = matcher.group(3);
            String hour = matcher.group(4);
            String minute = matcher.group(5);
            String second = matcher.group(6);

            // Format into "MMDDhhmmYYYY.ss"
            return String.format(Locale.getDefault(), "date %02d%02d%02d%02d%s.%02d",
                    Integer.parseInt(month), Integer.parseInt(day),
                    Integer.parseInt(hour), Integer.parseInt(minute),
                    year, Integer.parseInt(second));
        }

        // Return null if the pattern wasn't found
        return null;
    }

}