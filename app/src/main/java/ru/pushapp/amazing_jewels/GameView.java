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
    OnCustomListener mListener;

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

    HashSet<Integer> reward = new HashSet<>();

    boolean horizontalFlag = false;
    boolean verticalFlag = false;

    public void setCustomListener(OnCustomListener eventListener) {
        mListener = eventListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                horizontalFlag = false;
                verticalFlag = false;

                selectedCellX = (int) (event.getX() / CELL_SIZE);
                selectedCellY = (int) (event.getY() / CELL_SIZE);
                Log.d("GameViewTouchEvent", "Down");
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                nextXPos = (int) (event.getX() / CELL_SIZE);
                nextYPos = (int) (event.getY() / CELL_SIZE);

                if (!(horizontalFlag && verticalFlag)) {
                    if (selectedCellX + 2 > nextXPos && selectedCellX - 2 < nextXPos) {
                        horizontalFlag = true;
                        break;
                    } else {
                        horizontalFlag = false;
                        //todo return to previously place
                    }
                    if (selectedCellY + 2 > nextYPos && selectedCellY - 2 < nextYPos) {
                        verticalFlag = true;
                        break;
                    } else {
                        verticalFlag = false;
                }
            }


        }

/*                reward.clear();

                if (selectedCellX + 2 > nextXPos && selectedCellX - 2 < nextXPos) {
                    // Left to Right event
                    if (gameset[selectedCellY][selectedCellX].startX < gameset[selectedCellY][selectedCellX + 1].startX) {
                        gameset[selectedCellY][selectedCellX].startX = event.getX() - CELL_SIZE/2;
                    }
                    invalidate();
                    Log.d("GameViewTouchEvent", "Left to Right");

                    // Right to Left event
                    /// TODO: 21.02.2019
                    if (gameset[selectedCellY][selectedCellX].startX >= gameset[selectedCellY][selectedCellX - 1].startX) {
                        gameset[selectedCellY][selectedCellX].startX = event.getX() - CELL_SIZE/2;
                    }
                    invalidate();
                    Log.d("GameViewTouchEvent", "Right to Left");
                    break;
                }


                if (selectedCellY + 2 > nextYPos && selectedCellY - 2 < nextYPos) {
                    // UP to Down event
                    /// TODO: 21.02.2019
                    if (gameset[selectedCellY][selectedCellX].startY <= gameset[selectedCellY + 1][selectedCellX].startY) {
                        gameset[selectedCellY][selectedCellX].startY = event.getY() - CELL_SIZE/2;
                    }

                    invalidate();
                    Log.d("GameViewTouchEvent", "UP to Down");

                    // Down to UP event
                    /// TODO: 21.02.2019
                    if (gameset[selectedCellY][selectedCellX].startY >= gameset[selectedCellY - 1][selectedCellX].startY) {
                        gameset[selectedCellY][selectedCellX].startY = event.getY() - CELL_SIZE/2;
                    }

                    invalidate();
                    Log.d("GameViewTouchEvent", "Down to UP");
                }

                break;
            }*/
        case MotionEvent.ACTION_UP: {
            Bitmap buff;
            nextXPos = (int) (event.getX() / CELL_SIZE);
            nextYPos = (int) (event.getY() / CELL_SIZE);
            reward.clear();

            // Left to Right event
            if (selectedCellX < nextXPos) {

                float buff1 = gameset[selectedCellY][selectedCellX + 1].startX;
                float buff2 = gameset[selectedCellY][selectedCellX].startX;
//                    for (int i = 0; i < 10000000; i++){
//                        gameset[selectedCellY][selectedCellX + 1].startX -= CELL_SIZE/10000000;
//                        gameset[selectedCellY][selectedCellX].startX += CELL_SIZE/10000000;
//                        invalidate();
//                    }
                gameset[selectedCellY][selectedCellX + 1].startX = buff1;
                gameset[selectedCellY][selectedCellX].startX = buff2;


                buff = gameset[selectedCellY][selectedCellX + 1].bitmap;
                gameset[selectedCellY][selectedCellX + 1].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                gameset[selectedCellY][selectedCellX].bitmap = buff;

                invalidate();
                Log.d("GameViewTouchEvent", "Left to Right");
                checkCombo(selectedCellX, selectedCellY, selectedCellX + 1, selectedCellY);
                break;
            }

            // Right to Left event
            if (selectedCellX > nextXPos) {
                buff = gameset[selectedCellY][selectedCellX - 1].bitmap;
                gameset[selectedCellY][selectedCellX - 1].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                gameset[selectedCellY][selectedCellX].bitmap = buff;

                invalidate();
                Log.d("GameViewTouchEvent", "Right to Left");
                checkCombo(selectedCellX, selectedCellY, selectedCellX - 1, selectedCellY);
                break;
            }

            // UP to Down event
            if (selectedCellY < nextYPos) {
                buff = gameset[selectedCellY + 1][selectedCellX].bitmap;
                gameset[selectedCellY + 1][selectedCellX].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                gameset[selectedCellY][selectedCellX].bitmap = buff;

                invalidate();
                Log.d("GameViewTouchEvent", "UP to Down");
                checkCombo(selectedCellX, selectedCellY, selectedCellX, selectedCellY + 1);
                break;
            }

            // Down to UP event
            if (selectedCellY > nextYPos) {
                buff = gameset[selectedCellY - 1][selectedCellX].bitmap;
                gameset[selectedCellY - 1][selectedCellX].bitmap = gameset[selectedCellY][selectedCellX].bitmap;
                gameset[selectedCellY][selectedCellX].bitmap = buff;

                invalidate();
                Log.d("GameViewTouchEvent", "Down to UP");
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
        startY = 0;
        startX = 0;

        //draw fruits
        for (int i = 0; i < countY; i++, startX = 0, startY += CELL_SIZE) {
            for (int j = 0; j < countX; j++, startX += CELL_SIZE) {

//                canvas.drawBitmap(gameset[i][j].bitmap, startX + (CELL_SIZE / 2 - gameset[i][j].bitmap.getWidth() / 2), startY + (CELL_SIZE / 2 - gameset[i][j].bitmap.getHeight() / 2), darkCellPaint);
                Log.d("TESTinv", "draw");
                canvas.drawBitmap(gameset[i][j].bitmap, gameset[i][j].startX + (CELL_SIZE / 2 - gameset[i][j].bitmap.getWidth() / 2), gameset[i][j].startY + (CELL_SIZE / 2 - gameset[i][j].bitmap.getHeight() / 2), darkCellPaint);
            }
        }

    }

    private void checkCombo(int firstX, int firstY, int secondX, int secondY) {
        int deathFlag = 0;
        check(secondX, secondY);
        if (reward.size() > 2) {
            clearCombo(reward);
            addCoins(reward.size());
        } else {
            ++deathFlag;
            Log.d("GameViewReward", "Комбинаций не найдено");
        }
        reward.clear();


        check(firstX, firstY);
        if (reward.size() > 2) {
            clearCombo(reward);
            addCoins(reward.size());
        } else {
            ++deathFlag;
            Log.d("GameViewCombo", "Комбинаций не найдено");
        }
        reward.clear();

        if (deathFlag > 1) {
            removeLife(firstX, firstY, secondX, secondY);
        }
    }

    private void check(int secondX, int secondY) {
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
            check(secondX - count, secondY);
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
            check(secondX, secondY - count);
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

    private void clearCombo(HashSet<Integer> reward) {
        Log.d("GameViewCombo", "____________");
        Log.d("GameViewCombo", "countCell: " + reward.size());
        Log.d("GameViewCombo", "countCell: " + reward.toString());

        for (Integer i : reward) {
            int x = i % 10;
            int y = i / 10;
            gameset[y][x].bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icn_coin_sm)).getBitmap();
        }
    }

    private void addCoins(int rewardSetSize) {
        int coins = (rewardSetSize - 1) * 50;
        if (mListener != null)
            mListener.saveReward(coins);
        Log.d("GameViewReward", "Co-co-combo! +" + coins);
    }

    private void removeLife(int firstX, int firstY, int secondX, int secondY) {
        Bitmap buff;
        buff = gameset[firstY][firstX].bitmap;
        gameset[firstY][firstX].bitmap = gameset[secondY][secondX].bitmap;
        gameset[secondY][secondX].bitmap = buff;

        if (mListener != null)
            mListener.removeLife();
        Log.d("GameViewReward", "-1 life");
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

public interface OnCustomListener {
    void saveReward(int coins);

    void removeLife();
}
}
