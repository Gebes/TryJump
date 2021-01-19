package eu.gebes.tryjump;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.blocks.BlockManager;
import eu.gebes.tryjump.map.LoadMap;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Grid implements Disposable {

    BlockManager blockManager;
    private LoadMap loadMap= new LoadMap();

    public static Block[][][] blocks;

    public Grid() {
        blockManager = new BlockManager();
        blocks = loadMap.loadMap();


        /*Vector3 center = new Vector3(Variables.gridWidth/2f, Variables.gridHeight/2f, Variables.gridDepth/2f);
        int radius = 20;
        int innerRadius = 1;
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    Vector3 block = new Vector3(x,y,z);

                    float dst = block.dst2(center);
                    if(dst < (radius * radius) && dst > innerRadius * innerRadius)
                        blocks[x][y][z]= blockManager.getBlockFor(Block.Type.Log);

                }
            }
        }*/
        updatePosition();
    }

    public void updatePosition() {
        for (int i = 0; i < Variables.gridWidth; i++) {
            for (int j = 0; j < Variables.gridHeight; j++) {
                for (int k = 0; k < Variables.gridDepth; k++) {
                    float x = i * Variables.blockSize;
                    float y = j * Variables.blockSize;
                    float z = k * Variables.blockSize;
                    if (hasBlock(i, j, k))
                        blocks[i][j][k].setPosition(x, y, z);

                }
            }
        }

    }


    public void renderGrid(ModelBatch batch, Environment environment, CameraController cameraController) {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    if (hasBlock(x, y, z) && isVisible(x, y, z) && cameraController.getCameraWorldPosition().dst2(getBlock(x, y, z).getPosition().scl(1f / Variables.blockSize)) < (40*40))
                        batch.render(blocks[x][y][z].getInstance(), environment);
                }
            }
        }

    }

    public boolean hasBlock(int x, int y, int z) {
        return blocks[x][y][z] != null;
    }

    public void setBlock(int x, int y, int z, Block block) {
        blocks[x][y][z] = block;
    }

    public boolean isVisible(int x, int y, int z) {
        return !isHidden(x, y, z);
    }


    public boolean isHidden(int x, int y, int z) {
        return getBlock(x + 1, y, z) != null &&
               getBlock(x - 1, y, z) != null &&
               getBlock(x, y + 1, z) != null &&
               getBlock(x, y - 1, z) != null &&
               getBlock(x, y, z + 1) != null &&
               getBlock(x, y, z - 1) != null;
    }

    public Block getBlock(int x, int y, int z) {
        try {
            return blocks[x][y][z];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void dispose() {
        blockManager.dispose();
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                for (int z = 0; z < blocks[x][y].length; z++) {
                    if (hasBlock(x, y, z))
                        blocks[x][y][z].dispose();
                }
            }
        }
    }

    public void editBoxByRayCast(Vector3 startPoint, Vector3 direction, Block.Type type) {
        int lastPointX = 0;
        int lastPointY = 0;
        int lastPointZ = 0;

        for (int i = 1; i < Variables.blockSize*10; i++) {
            Vector3 tmpStart = new Vector3(startPoint);
            Vector3 tmpDirection = new Vector3(direction);
            tmpDirection.nor();
            tmpDirection.scl(i);
            Vector3 line = tmpStart.add(tmpDirection);
            // scale to grid world
            line.scl(1f / Variables.blockSize);
            int x = Math.round(line.x);
            int y = Math.round(line.y);
            int z = Math.round(line.z);

            if (x > (Variables.gridWidth - 1) || y > (Variables.gridHeight - 1) || z > (Variables.gridDepth - 1) || x < 0 || y < 0 || z < 0) {
                break;
            }

            if (blocks[x][y][z] != null) {
                if (type == null) {
                    if (blocks[x][y][z] != null) {
                        blocks[x][y][z].dispose();
                        blocks[x][y][z] = null;
                        updatePosition();
                    }
                } else if (type == type) {
                    blocks[lastPointX][lastPointY][lastPointZ] = blockManager.getBlockFor(type);
                    updatePosition();
                }
                break;
            }

            lastPointX = x;
            lastPointY = y;
            lastPointZ = z;
        }
    }


}
