package com.example.ez_inventory_system;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.SoundPool;

import static com.example.ez_inventory_system.GameView.screenRatioX;
import static com.example.ez_inventory_system.GameView.screenRatioY;

public class Inventory {

    public int speed = 20;
    public boolean wasShot = true;
    int x = 0, y, width, height, inventoryCounter = 1;
    Bitmap inventory1, inventory2, inventory3, inventory4;
    private SoundPool soundPool;
    private int sound;

    Inventory (Resources res) {

        inventory1 = BitmapFactory.decodeResource(res, R.drawable.inventory1);
        inventory2 = BitmapFactory.decodeResource(res, R.drawable.inventory2);
        inventory3 = BitmapFactory.decodeResource(res, R.drawable.inventory3);
        inventory4 = BitmapFactory.decodeResource(res, R.drawable.inventory4);

        width = inventory1.getWidth();
        height = inventory1.getHeight();

        width /= 8;
        height /= 8;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        inventory1 = Bitmap.createScaledBitmap(inventory1, width, height, false);
        inventory2 = Bitmap.createScaledBitmap(inventory2, width, height, false);
        inventory3 = Bitmap.createScaledBitmap(inventory3, width, height, false);
        inventory4 = Bitmap.createScaledBitmap(inventory4, width, height, false);

        y = -height;
    }

    Bitmap getInventory () {

        if (inventoryCounter == 1) {
            inventoryCounter++;
            return inventory1;
        }

        if (inventoryCounter == 2) {
            inventoryCounter++;
            return inventory2;
        }

        if (inventoryCounter == 3) {
            inventoryCounter++;
            return inventory3;
        }

        inventoryCounter = 1;

        return inventory4;
    }

    Rect getCollisionShape () {

        return new Rect(x, y, x + width, y + height);
    }

}
