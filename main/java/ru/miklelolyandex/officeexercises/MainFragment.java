package ru.miklelolyandex.officeexercises;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mike on 05.12.17.
 */

public class MainFragment extends Fragment {
    private ImageView image1;
    private ImageView image2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image1 = view.findViewById(R.id.main_card1);
        image2 = view.findViewById(R.id.main_card2);
        setBackground();
        view.findViewById(R.id.main_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrainingActivity.class);
                intent.putExtra("cardNum", 1);
                intent.putExtra("imageName", "");
                intent.putExtra("amountOfExercises", 10);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.main_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrainingActivity.class);
                intent.putExtra("cardNum", 2);
                intent.putExtra("imageName", "a");
                intent.putExtra("amountOfExercises", 7);
                startActivity(intent);
            }
        });
    }

    private void setBackground() {
        AssetManager assetManager = getContext().getAssets();
        String path = "1/" + String.valueOf(1) +".png";
        try {
            InputStream is = assetManager.open(path);
            Drawable drawable =Drawable.createFromStream(is, null);
            image1.setImageDrawable(drawable);
            path = "2/a0.png";
            is = assetManager.open(path);
            drawable = Drawable.createFromStream(is, null);
            image2.setImageDrawable(drawable);
            is.close();
        }
        catch(IOException ex)
        {
            return;
        }
    }

}
