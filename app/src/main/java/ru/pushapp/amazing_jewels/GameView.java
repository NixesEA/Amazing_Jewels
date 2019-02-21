package ru.pushapp.amazing_jewels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

public class GameView extends View {

    int countX = 0;
    int countY = 0;

    float CELL_SIZE;
    float WIDTH_SCREEN;
    float HEIGHT_SCREEN;

    int selectedCellX = -1;
    int selectedCellY = -1;

    int nextXPos;
    int nextYPos;

    Paint topDarkGrad = new Paint();
    Paint topLightGrad = new Paint();

    Paint lightCellPaint = new Paint();
    Paint darkCellPaint = new Paint();


    int[] prefabRes = {R.drawable.fruit_1, R.drawable.fruit_2,
            R.drawable.fruit_3, R.drawable.fruit_4,
            R.drawable.fruit_5, R.drawable.fruit_6,
            R.drawable.fruit_7, R.drawable.fruit_8};
    Bitmap[] prefab = new Bitmap[8];
    GameUnit[][] gameset;

//    HashMap<Integer, String> reward = new HashMap<>();
    HashSet<Integer> reward = new HashSet<>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                selectedCellX = (int) (event.getX() / CELL_SIZE);
                selectedCellY = (int) (event.getY() / CELL_SIZE);
                Log.d("TESTtouch", "Down");
                break;
            }
            case MotionEvent.ACTION_UP: {
                Bitmap buff;
                nextXPos = (int) (event.getX() / CELL_SIZE);
                nextYPos = (int) (event.getY() / CELL_SIZE);
                reward.clear();

                //if left to right sweep event on screen
                if (selectedCellX < nextXPos) {
                    buff = gameset[selectedCellY][selectedCellX + 1].bitmap;
                    gameset[selectedCellY][selectedCellX + 1].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                    gameset[selectedCellY][selectedCellX].bitmap = buff;

                    invalidate();
                    Log.d("TESTtouch", "Left to Right");
                    checkCombo(selectedCellX, selectedCellY, selectedCellX + 1, selectedCellY);
                    break;
                }

                // if right to left sweep event on screen
                if (selectedCellX > nextXPos) {
                    buff = gameset[selectedCellY][selectedCellX - 1].bitmap;
                    gameset[selectedCellY][selectedCellX - 1].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                    gameset[selectedCellY][selectedCellX].bitmap = buff;

                    invalidate();
                    Log.d("TESTtouch", "Right to Left");
                    checkCombo(selectedCellX, selectedCellY, selectedCellX - 1, selectedCellY);
                    break;
                }

                // if UP to Down sweep event on screen
                if (selectedCellY < nextYPos) {
                    buff = gameset[selectedCellY + 1][selectedCellX].bitmap;
                    gameset[selectedCellY + 1][selectedCellX].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                    gameset[selectedCellY][selectedCellX].bitmap = buff;

                    invalidate();
                    Log.d("TESTtouch", "UP to Down");
                    checkCombo(selectedCellX, selectedCellY, selectedCellX, selectedCellY + 1);
                    break;
                }

                //if Down to UP sweep event on screen
                if (selectedCellY > nextYPos) {
                    buff = gameset[selectedCellY - 1][selectedCellX].bitmap;
                    gameset[selectedCellY - 1][selectedCellX].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                    gameset[selectedCellY][selectedCellX].bitmap = buff;

                    invalidate();
                    Log.d("TESTtouch", "Down to UP");
                    checkCombo(selectedCellX, selectedCellY, selectedCellX, selectedCellY - 1);
                    break;
                }
                break;
            }
        }

        return true;
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //gradient
//        LinearGradient lightGradient = new LinearGradient(0, 0, 200, 200, getResources().getColor(R.color.start_dark_cell),
//                getResources().getColor(R.color.stop_dark_cell), Shader.TileMode.CLAMP);
//        topLightGrad.setDither(true);
//        topLightGrad.setShader(lightGradient);

//        LinearGradient darkGradient = new LinearGradient(0, 0, 200, 200, getResources().getColor(R.color.start_dark_cell),
//                getResources().getColor(R.color.stop_dark_cell), Shader.TileMode.CLAMP);
//        topDarkGrad.setDither(true);
//        topDarkGrad.setShader(darkGradient);

        lightCellPaint.setColor(getResources().getColor(R.color.light_cell));
        darkCellPaint.setColor(getResources().getColor(R.color.dark_cell));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        WIDTH_SCREEN = getMeasuredWidth();
        HEIGHT_SCREEN = getMeasuredHeight();
        CELL_SIZE = WIDTH_SCREEN / 6;

        countX = (int) (WIDTH_SCREEN / CELL_SIZE);
        countY = (int) (HEIGHT_SCREEN / CELL_SIZE) - 1;


        for (int i = 0; i < prefab.length; i++) {
            Drawable d = getResources().getDrawable(prefabRes[i]);
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            prefab[i] = b;
        }


        try {
            gameset = new GameUnit[countY][countX];
            float startX = 0;
            float startY = 0;

            for (int i = 0; i < countY; i++) {
                int[] line = generateLine(i);
                for (int j = 0; j < countX; j++) {
                    int index = line[j];
                    gameset[i][j] = new GameUnit();
                    gameset[i][j].bitmap = prefab[index];
                    gameset[i][j].startX = startX;
                    gameset[i][j].startY = startY;

                    startX += CELL_SIZE;
                }
                startX = 0;
                startY += CELL_SIZE;
            }

        } catch (NegativeArraySizeException ignored) {
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startX = 0;
        float startY = 0;

        //draw chess board
        for (int i = 0; i < countY; i++, startX = 0, startY += CELL_SIZE) {
            for (int j = 0; j < countX; j++, startX += CELL_SIZE) {

                if ((i + j) % 2 == 0) {
                    canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, lightCellPaint);
                } else {
                    canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, darkCellPaint);
                }
            }
        }
        startX = 0;
        startY = 0;

        //draw fruit
        for (int i = 0; i < countY; i++, startX = 0, startY += CELL_SIZE) {
            for (int j = 0; j < countX; j++, startX += CELL_SIZE) {

//                canvas.drawBitmap(gameset[i][j].bitmap, startX + (CELL_SIZE / 2 - gameset[i][j].bitmap.getWidth() / 2), startY + (CELL_SIZE / 2 - gameset[i][j].bitmap.getHeight() / 2), darkCellPaint);
                canvas.drawBitmap(gameset[i][j].bitmap, gameset[i][j].startX + (CELL_SIZE / 2 - gameset[i][j].bitmap.getWidth() / 2), gameset[i][j].startY + (CELL_SIZE / 2 - gameset[i][j].bitmap.getHeight() / 2), darkCellPaint);
            }
        }

    }

    private void checkCombo(int firstX, int firstY, int secondX, int secondY) {
        check(secondX,secondY);
        if (reward.size() > 2) {
            Log.d("TESTbit", "____________");
            Log.d("TESTbit", "countCell: " + reward.size());
            Log.d("TESTbit", "countCell: " + reward.toString());
            Log.d("TESTreward", "Co-co-combo! +" + ((reward.size() - 1) * 50));

            for (Integer i : reward) {
                int x = i % 10;
                int y = i / 10;
                gameset[y][x].bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icn_coin_sm)).getBitmap();
            }
        } else {
            Log.d("TESTbit", "Комбинаций не найдено");
        }
        reward.clear();


        check(firstX,firstY);
        if (reward.size() > 2) {
            Log.d("TESTbit", "____________");
            Log.d("TESTbit", "countCell: " + reward.size());
            Log.d("TESTbit", "countCell: " + reward.toString());
            Log.d("TESTreward", "Co-co-combo! +" + ((reward.size() - 1) * 50));

            for (Integer i : reward) {
                int x = i % 10;
                int y = i / 10;
                gameset[y][x].bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icn_coin_sm)).getBitmap();
            }

        } else {
            Log.d("TESTbit", "Комбинаций не найдено");
        }

    }

    private void check(int secondX, int secondY){
        boolean right = true;
        boolean left = true;
        boolean top = true;
        boolean bottom = true;

        reward.add(secondY * 10 + secondX);

        int count = 1;
        while (right) {
            if (secondX + count >= countX) {
                right = false;
                continue;
            }

            if (reward.contains(secondY * 10 + (secondX + count))) {
                break;
            }
            if (gameset[secondY][secondX].bitmap != gameset[secondY][secondX + count].bitmap) {
                right = false;
                continue;
            }
            check(secondX + count, secondY);
            reward.add(secondY * 10 + (secondX + count));
            count++;
        }

        count = 1;
        while (left) {
            if (secondX - count <= -1) {
                left = false;
                continue;
            }
            if (reward.contains(secondY * 10 + (secondX - count))) {
                break;
            }
            if (gameset[secondY][secondX].bitmap != gameset[secondY][secondX - count].bitmap) {
                left = false;
                continue;
            }
            check(secondX - count,secondY);
            reward.add(secondY * 10 + (secondX - count));
            count++;
        }

        count = 1;
        while (top) {
            if (secondY - count <= -1) {
                top = false;
                continue;
            }
            if (reward.contains((secondY - count) * 10 + secondX)) {
                break;
            }
            if (gameset[secondY][secondX].bitmap != gameset[secondY - count][secondX].bitmap) {
                top = false;
                continue;
            }
            check(secondX,secondY - count);
            reward.add((secondY - count) * 10 + secondX);
            count++;
        }

        count = 1;
        while (bottom) {
            if (secondY + count >= countY) {
                bottom = false;
                continue;
            }
            if (reward.contains((secondY + count) * 10 + secondX)) {
                break;
            }
            if (gameset[secondY][secondX].bitmap != gameset[secondY + count][secondX].bitmap) {
                bottom = false;
                continue;
            }
            check(secondX, secondY + count);
            reward.add((secondY + count) * 10 + secondX);
            count++;
        }
    }

    private int[] generateLine(int y) {
        int[] line = new int[countX];
        for (int i = 0; i < line.length; i++) {
            line[i] = (int) (Math.random() * countX);
        }
        return line;
    }


    private class GameUnit {

        public Bitmap bitmap;
        public float startX;
        public float startY;

    }
}
