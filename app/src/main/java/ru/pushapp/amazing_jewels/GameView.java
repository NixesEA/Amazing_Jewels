package ru.pushapp.amazing_jewels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

public class GameView extends View {
    final int ANIM_DURATION = 300;
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

    private GradientDrawable mDrawable;
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
            case MotionEvent.ACTION_UP: {
                nextXPos = (int) (event.getX() / CELL_SIZE);
                nextYPos = (int) (event.getY() / CELL_SIZE);
                reward.clear();

                // Left to Right event
                if (selectedCellX < nextXPos) {

                    animX(selectedCellX, selectedCellX + 1, true);
                    Log.d("GameViewTouchEvent", "Left to Right");
                    break;
                }

                // Right to Left event
                if (selectedCellX > nextXPos) {

                    animX(selectedCellX, selectedCellX - 1, true);
                    Log.d("GameViewTouchEvent", "Right to Left");
                    break;
                }

                // UP to Down event
                if (selectedCellY < nextYPos) {

                    animY(selectedCellY, selectedCellY + 1, true);
                    Log.d("GameViewTouchEvent", "UP to Down");
                    break;
                }

                // Down to UP event
                if (selectedCellY > nextYPos) {

                    animY(selectedCellY, selectedCellY - 1, true);
                    Log.d("GameViewTouchEvent", "Down to UP");
                    break;
                }
                break;
            }
        }

        return true;
    }

    private void animY(final int fY, final int sY, final boolean check) {
        final float buff1 = gameset[sY][selectedCellX].startY;
        final float buff2 = gameset[fY][selectedCellX].startY;

        ValueAnimator animatorY = ValueAnimator.ofFloat(buff1, buff2);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                gameset[sY][selectedCellX].startY = (float) animation.getAnimatedValue();
                gameset[fY][selectedCellX].startY = buff1 - ((float) animation.getAnimatedValue() - buff2);
                invalidate();
            }
        });
        animatorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gameset[sY][selectedCellX].startY = buff1;
                gameset[fY][selectedCellX].startY = buff2;

                Bitmap buff = gameset[sY][selectedCellX].bitmap;
                gameset[sY][selectedCellX].bitmap = gameset[fY][selectedCellX].bitmap;
                gameset[fY][selectedCellX].bitmap = buff;

                if (check) {
                    checkCombo(selectedCellX, fY, selectedCellX, sY);
                }
            }
        });
        animatorY.setDuration(ANIM_DURATION).start();
    }

    private void animX(final int fX, final int sX, final boolean check) {
        final float buff1 = gameset[selectedCellY][sX].startX;
        final float buff2 = gameset[selectedCellY][fX].startX;

        ValueAnimator animatorX = ValueAnimator.ofFloat(buff1, buff2);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                gameset[selectedCellY][sX].startX = (float) animation.getAnimatedValue();
                gameset[selectedCellY][fX].startX = buff1 - ((float) animation.getAnimatedValue() - buff2);
                invalidate();
            }
        });
        animatorX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gameset[selectedCellY][sX].startX = buff1;
                gameset[selectedCellY][fX].startX = buff2;

                Bitmap buff = gameset[selectedCellY][sX].bitmap;
                gameset[selectedCellY][sX].bitmap = gameset[selectedCellY][fX].bitmap;
                gameset[selectedCellY][fX].bitmap = buff;

                if (check) {
                    checkCombo(fX, selectedCellY, sX, selectedCellY);
                }
            }
        });
        animatorX.setDuration(ANIM_DURATION).start();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < prefab.length; i++) {
            Drawable d = getResources().getDrawable(prefabRes[i]);
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            prefab[i] = b;
        }

        //gradient

        mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{0xFFFF0000, 0xFF00FF00,
                        0xFF0000FF});
        mDrawable.setShape(GradientDrawable.RECTANGLE);

        lightCellPaint.setColor(getResources().getColor(R.color.light_cell));
        darkCellPaint.setColor(getResources().getColor(R.color.dark_cell));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        WIDTH_SCREEN = getMeasuredWidth();
        HEIGHT_SCREEN = getMeasuredHeight();
        CELL_SIZE = WIDTH_SCREEN / 6;

        countY = ((int) (HEIGHT_SCREEN / CELL_SIZE)) - 1;
        countX = (int) (WIDTH_SCREEN / CELL_SIZE);


        if (gameset == null || gameset.length != countY) {
            generateGameSet();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw chess board
        float startX = 0;
        float startY = 0;
        for (int i = 0; i < countY; i++, startX = 0, startY += CELL_SIZE) {
            for (int j = 0; j < countX; j++, startX += CELL_SIZE) {

                if ((i + j) % 2 == 0) {
                    if (i == 0) {
                        topLightGrad.setShader(new LinearGradient(startX + CELL_SIZE / 2, startY, startX + CELL_SIZE / 2, startY + CELL_SIZE, getResources().getColor(R.color.stop_light_cell), getResources().getColor(R.color.start_light_cell), Shader.TileMode.MIRROR));
                        canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, topLightGrad);
                    } else if (i == countY - 1) {
                        topLightGrad.setShader(new LinearGradient(startX + CELL_SIZE / 2, startY, startX + CELL_SIZE / 2, startY + CELL_SIZE, getResources().getColor(R.color.start_light_cell), getResources().getColor(R.color.stop_light_cell), Shader.TileMode.MIRROR));
                        canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, topLightGrad);
                    } else {
                        canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, lightCellPaint);
                    }
                } else {
                    if (i == 0) {
                        topDarkGrad.setShader(new LinearGradient(startX + CELL_SIZE / 2, startY, startX + CELL_SIZE / 2, startY + CELL_SIZE, getResources().getColor(R.color.stop_dark_cell), getResources().getColor(R.color.start_dark_cell), Shader.TileMode.MIRROR));
                        canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, topDarkGrad);
                    } else if(i == countY - 1){
                        topDarkGrad.setShader(new LinearGradient(startX + CELL_SIZE / 2, startY, startX + CELL_SIZE / 2, startY + CELL_SIZE, getResources().getColor(R.color.start_dark_cell), getResources().getColor(R.color.stop_dark_cell), Shader.TileMode.MIRROR));
                        canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, topDarkGrad);
                    } else {
                        canvas.drawRect(startX, startY, startX + CELL_SIZE, startY + CELL_SIZE, darkCellPaint);
                    }
                }
            }
        }

        //Pick up coins
        startY = 0;
        startX = 0;
        Bitmap coin = ((BitmapDrawable) getResources().getDrawable(R.drawable.icn_coin_sm)).getBitmap();
        for (
                int i = 0;
                i < countY; i++, startX = 0, startY += CELL_SIZE) {
            for (int j = 0; j < countX; j++, startX += CELL_SIZE) {
                if (gameset[i][j].bitmap == coin) {
                    try {
                        if (gameset[i - 1][j].bitmap != coin) {

                            gameset[i][j].bitmap = gameset[i - 1][j].bitmap;
                            gameset[i - 1][j].bitmap = coin;

                            i = 0;
                            j = 0;
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }

        //draw fruits
        startY = 0;
        startX = 0;
        for (
                int i = 0;
                i < countY; i++, startX = 0, startY += CELL_SIZE) {
            for (int j = 0; j < countX; j++, startX += CELL_SIZE) {
                if (gameset[i][j].bitmap == coin) {
                    int index = (int) (Math.random() * countX);
                    gameset[i][j].bitmap = prefab[index];
                }

                canvas.drawBitmap(gameset[i][j].bitmap, gameset[i][j].startX + (CELL_SIZE / 2 - gameset[i][j].bitmap.getWidth() / 2), gameset[i][j].startY + (CELL_SIZE / 2 - gameset[i][j].bitmap.getHeight() / 2), darkCellPaint);
            }
        }

    }

    public void generateGameSet() {
        try {
            gameset = new GameUnit[countY][countX];
            float startX = 0;
            float startY = 0;

            for (int i = 0; i < countY; i++) {
                int[] line = generateLine();
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

            invalidate();
        } catch (NegativeArraySizeException ignored) {
        } catch (NullPointerException ignored) {
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

    private void check(int x, int y) {
        boolean right = true;
        boolean left = true;
        boolean top = true;
        boolean bottom = true;

        reward.add(y * 10 + x);

        int count = 1;
        while (right) {
            if (x + count >= countX) {
                right = false;
                continue;
            }

            if (reward.contains(y * 10 + (x + count))) {
                right = false;
                continue;

            }
            if (!gameset[y][x].bitmap.equals(gameset[y][x + count].bitmap)) {
                right = false;
                continue;
            }
            reward.add(y * 10 + (x + count));
            check(x + count, y);
            count++;
        }

        count = 1;
        while (left) {
            if (x - count <= -1) {
                left = false;
                continue;
            }
            if (reward.contains(y * 10 + (x - count))) {
                left = false;
                continue;
            }
            if (!gameset[y][x].bitmap.equals(gameset[y][x - count].bitmap)) {
                left = false;
                continue;
            }
            reward.add(y * 10 + (x - count));
            check(x - count, y);
            count++;
        }

        count = 1;
        while (top) {
            if (y - count <= -1) {
                top = false;
                continue;
            }
            if (reward.contains((y - count) * 10 + x)) {
                top = false;
                continue;
            }
            if (!gameset[y][x].bitmap.equals(gameset[y - count][x].bitmap)) {
                top = false;
                continue;
            }
            reward.add((y - count) * 10 + x);
            check(x, y - count);
            count++;
        }

        count = 1;
        while (bottom) {
            if (y + count >= countY) {
                bottom = false;
                continue;
            }
            if (reward.contains((y + count) * 10 + x)) {
                bottom = false;
                continue;
            }
            if (!gameset[y][x].bitmap.equals(gameset[y + count][x].bitmap)) {
                bottom = false;
                continue;
            }
            reward.add((y + count) * 10 + x);
            check(x, y + count);
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

        if (secondX == firstX) {
            animY(secondY, firstY, false);
        } else {
            animX(secondX, firstX, false);
        }

        if (mListener != null)
            mListener.removeLife();
        Log.d("GameViewReward", "-1 life");
    }

    private int[] generateLine() {
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
