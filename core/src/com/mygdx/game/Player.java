package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by William on 11/21/2015.
 */
public class Player extends GameObject{
    public static final int NUM_PLAYER_FRAMES= 16;

    public boolean canGetStick() {
        return sticks < MAX_STICKS;
    }

    public boolean canLightTorch() {
        if(sticks > 0)
        {
            if(lastFireDist < Fire.FireDist)
            {
                return true;
            }
            else if(tinderboxes > 0)
            {
                return true;
            }
        }
        return false;

    }
    public boolean canLightFire()
    {
        if(sticks >= 2)
        {
            if(torch.isLit())
            {
                return true;
            }
            else if(tinderboxes > 0)
            {
                return true;
            }
        }
    ;    return false;
    }

    public void useBranches(int i)
    {
        sticks-=i;
    }
    public void useTinderboxes(int i) {
        tinderboxes-=i;
    }

    public int getTinderboxes() {
        return tinderboxes;
    }

    public int getSticks() {
        return sticks;
    }

    enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    };

    private Direction direction = Direction.DOWN;
    boolean moving;
    float t;
    private Animation playerUpAnimation;
    private Animation playerDownAnimation;
    private Animation playerLeftAnimation;
    private Animation playerRightAnimation;
    float lastFireDist;
    private float speed, heat;
    private int sticks;
    private float[] position = new float[2];
    private float[] startPosition = new float[2];
    public final float MAX_SPEED =  2f;
    public final float MIN_SPEED = 0.75f;
    public final float MAX_HEAT =  100;
    public final float MAX_DISTANCE_WARM = 256;
    public final float MAX_DISTANCE_HOLD = 384;
    public final float START_WARM = 65;
    public final float START_FREEZE = 30;
    private boolean alive, mapFound;

    public final int MAX_STICKS = 3;
    public  int tinderboxes = 3;
    private Torch torch = new Torch(0);




    public Player(Texture playerTexture, float startX, float startY)
    {
        super(null);
        TextureRegion tmp[][] = TextureRegion.split(playerTexture,playerTexture.getHeight(),playerTexture.getHeight());
        TextureRegion upTex[] = new TextureRegion[4];
        TextureRegion downTex[] = new TextureRegion[4];
        TextureRegion leftTex[] = new TextureRegion[4];
        TextureRegion rightTex[] = new TextureRegion[4];

        for(int i = 0; i < 4; i++)
        {
            downTex[i] = tmp[0][i];
            rightTex[i] = tmp[0][i + 4];
            upTex[i] = tmp[0][i + 8];
            leftTex[i] = tmp[0][i + 12];

        }
        playerUpAnimation = new Animation(0.2f,upTex);
        playerDownAnimation = new Animation(0.2f,downTex);
        playerLeftAnimation = new Animation(0.2f,leftTex);
        playerRightAnimation = new Animation(0.2f,rightTex);

        super.setSprite(new Sprite(playerUpAnimation.getKeyFrame(0.0f)));
        super.getSprite().setOrigin(sprite.getWidth()/2.0f,0.0f);
        super.getSprite().setScale(1.5f);
        speed = MAX_SPEED;
        heat = MAX_HEAT;
        alive = true;
        startPosition[0] = startX;
        startPosition[1] = startY;
        setPosition(startX, startY);

    }

    public void render(SpriteBatch sb)
    {
        if(torch.isLit())
        {
            torch.torchSprite.setPosition(sprite.getX(),sprite.getY() - sprite.getHeight()/2);
            torch.torchSprite.setRotation(sprite.getRotation());
            torch.render(sb);

        }
        super.render(sb);

    }

    public float getSpeed()
    {
        return this.speed;
    }

    public float getHeat()
    {
        return this.heat;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }


    public void update(float dt)
    {

        setPosition(position[0],position[1]);
        animate(dt);
        updateShadow();
        if(torch.isLit())
        {
            torch.update(dt);
        }
    }

    public void moveAnim(float x, float y)
    {moving = true;
        if(x < 0)
        {
            direction = Direction.LEFT;
        }
        else if(x > 0)
        {
            direction = Direction.RIGHT;
        }
        else if(y > 0)
        {
            direction = Direction.UP;
        }
        else if(y < 0)
        {
            direction = Direction.DOWN;
        }
        else
        {
            moving = false;
        }
    }
    public void move(float x, float y, float xBound, float yBound)
    {



        if(position[0] > 0)
            position[0] += x;
        else
            position[0] = 0;

        if(position[0] < xBound)
            position[0] += x;
        else
            position[0] = xBound;

        if(position[1] > 0)
            position[1] += y;
        else
            position[1] = 0;

        if(position[1] < yBound)
            position[1] += y;
        else
            position[1] = yBound;
    }

    private void animate(float dt) {
        if(moving)
        {
            t += dt;
        }
        else
        {
            t = 0.19f;
        }
        switch(direction)
        {
            case UP:
                getSprite().setRegion(playerUpAnimation.getKeyFrame(t,true));
                break;
            case LEFT:
                getSprite().setRegion(playerLeftAnimation.getKeyFrame(t,true));
                break;
            case DOWN:
                getSprite().setRegion(playerDownAnimation.getKeyFrame(t,true));
                break;
            case RIGHT:
                getSprite().setRegion(playerRightAnimation.getKeyFrame(t,true));
                break;
        }
    }

    public boolean overStick(Stick stick)
    {
        return stick.getSprite().getBoundingRectangle().overlaps(this.getSprite().getBoundingRectangle());
    }

    public void overMap(GoalMap map)
    {
        if(map.getSprite().getBoundingRectangle().overlaps(this.getSprite().getBoundingRectangle()))
            mapFound = true;
        //return map.getSprite().getBoundingRectangle().overlaps(this.getSprite().getBoundingRectangle());
    }

    public boolean foundMap()
    {
        return mapFound;
    }

    public boolean returnedHome()
    {
        float minStartX = this.startPosition[0] - 50;
        float maxStartX = this.startPosition[0] + 50;
        float minStartY = this.startPosition[1] - 50;
        float maxStartY = this.startPosition[1] + 50;

        boolean withinX = ((this.getX() < maxStartX) && (this.getX() > minStartX));
        boolean withinY = ((this.getY() < maxStartY) && (this.getY() > minStartY));

        boolean atHome = (withinX && withinY);
        System.out.println(this.startPosition[0] + " " + this.startPosition[1]);
        System.out.println(this.getX() + " " + this.getY());
        return mapFound && atHome;
    }

    public boolean pickUpStick(Stick stick)
    {
        if(overStick(stick) && sticks < MAX_STICKS)
        {

            sticks++;
            return true;
        }
        return false;
    }
    @Override
    public void setPosition(float x, float y)
    {
        position[0] = x;
        position[1] = y;
        super.setPosition(x,y);
    }

    public void fireWarm(float distance)
    {
        //Check lighting distance for torch
        lastFireDist = distance;
        //System.out.println(distance);
        if(heat < MAX_HEAT && distance < MAX_DISTANCE_WARM) {
            //System.out.println("hi");
            heat += (10.0/60.0);
            //System.out.println("warm: " + heat);
        }
        if(heat > 0 && distance > MAX_DISTANCE_HOLD)
        {
            //torch reduces heat loss in 1/5th
            heat -= ((torch.isLit() ? 1.0 : 5.0)/60.0);
        }

        if(heat <= 0)
        {
            System.out.println("DEAD");
            alive = false;
        }
        //System.out.println("heat: " + heat);
    }

    public void freezeSlowdown()
    {
        if(heat > START_WARM && speed < MAX_SPEED)
        {
            speed += 0.05f;
        }

        if(heat < START_FREEZE && speed > MIN_SPEED)
        {
            speed -= 0.05f;
        }
    }

    public void lightTorch()
    {
        if(sticks > 0)
        {
            if(torch.isLit() || lastFireDist < Fire.FireDist)
            {

                torch = new Torch();
                sticks--;

            }
            else if(tinderboxes > 0)
            {

                tinderboxes--;
                torch = new Torch();
                sticks--;
            }
        }
        else
        {
            System.out.println("No sticks");
        }
    }
    public Torch getTorch()
    {
        return torch;
    }

    public boolean getLifeStatus()
    {
        return alive;
    }
}
