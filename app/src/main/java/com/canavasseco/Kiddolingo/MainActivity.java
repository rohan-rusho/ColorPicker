package com.canavasseco.Kiddolingo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private CardView colorCard;
    private TextView colorHex;
    private Button generateBtn, copyBtn, shareBtn, pickColorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorCard = findViewById(R.id.colorCard);
        colorHex = findViewById(R.id.colorHex);
        generateBtn = findViewById(R.id.generateBtn);
        copyBtn = findViewById(R.id.copyBtn);
        shareBtn = findViewById(R.id.shareBtn);
        pickColorBtn = findViewById(R.id.pickColorBtn);

        // Random Color Generator
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hex = getRandomColor();
                updateColor(hex);
            }
        });

        // Pick Color Dialog
        pickColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initialColor = Color.WHITE;
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(MainActivity.this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        String hex = String.format("#%06X", (0xFFFFFF & color));
                        updateColor(hex);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // Do nothing
                    }
                });
                colorPicker.show();
            }
        });

        // Copy Color
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hex = colorHex.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Color Hex", hex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied " + hex, Toast.LENGTH_SHORT).show();
            }
        });

        // Share Color
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hex = colorHex.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this color: " + hex + " 🎨");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
    }

    // Generate random hex color
    private String getRandomColor() {
        Random rnd = new Random();
        int color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return String.format("#%06X", (0xFFFFFF & color));
    }

    // Update UI color and text contrast
    private void updateColor(String hex) {
        colorHex.setText(hex);
        colorCard.setCardBackgroundColor(Color.parseColor(hex));

        if (isColorDark(hex)) {
            colorHex.setTextColor(Color.WHITE);
        } else {
            colorHex.setTextColor(Color.BLACK);
        }
    }

    // Check if color is dark
    private boolean isColorDark(String hexColor) {
        int color = Color.parseColor(hexColor);
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}
