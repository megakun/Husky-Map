package seamcarving;

import edu.princeton.cs.algs4.Picture;

public class DualGradientEnergyFunction implements EnergyFunction {
    @Override
    public double apply(Picture picture, int x, int y) {
        if (!Utils.inBounds(x, y, picture.width(), picture.height())) {
            throw new IndexOutOfBoundsException();
        }
        return Math.pow((deltaX(picture, x, y) + deltaY(picture, x, y)), 0.5);

    }

    private double deltaX(Picture picture, int x, int y) {
        int left = x - 1;
        int right = x + 1;
        double deltaR;
        double deltaG;
        double deltaB;
        if (x == 0) {
            int right2 = x + 2;
            deltaR = Math.pow((-3 * picture.get(x, y).getRed() + 4 * picture.get(right, y).getRed() -
                picture.get(right2, y).getRed()), 2);
            deltaG = Math.pow((-3 * picture.get(x, y).getGreen() + 4 * picture.get(right, y).getGreen() -
                picture.get(right2, y).getGreen()), 2);
            deltaB = Math.pow((-3 * picture.get(x, y).getBlue() + 4 * picture.get(right, y).getBlue() -
                picture.get(right2, y).getBlue()), 2);
        } else if (x == picture.width() - 1) {
            int left2 = x - 2;
            deltaR = Math.pow((-3 * picture.get(x, y).getRed() + 4 * picture.get(left, y).getRed() -
                picture.get(left2, y).getRed()), 2);
            deltaG = Math.pow((-3 * picture.get(x, y).getGreen() + 4 * picture.get(left, y).getGreen() -
                picture.get(left2, y).getGreen()), 2);
            deltaB = Math.pow((-3 * picture.get(x, y).getBlue() + 4 * picture.get(left, y).getBlue() -
                picture.get(left2, y).getBlue()), 2);
        } else {
            deltaR = Math.pow((picture.get(left, y).getRed() - picture.get(right, y).getRed()), 2);
            deltaG = Math.pow((picture.get(left, y).getGreen() - picture.get(right, y).getGreen()), 2);
            deltaB = Math.pow((picture.get(left, y).getBlue() - picture.get(right, y).getBlue()), 2);
        }

        return deltaR + deltaG + deltaB;

    }

    private double deltaY(Picture picture, int x, int y) {
        int up = y - 1;
        int down = y + 1;
        double deltaR;
        double deltaG;
        double deltaB;
        if (y == 0) {
            int down2 = y + 2;
            deltaR = Math.pow((-3 * picture.get(x, y).getRed() + 4 * picture.get(x, down).getRed() -
                picture.get(x, down2).getRed()), 2);
            deltaG = Math.pow((-3 * picture.get(x, y).getGreen() + 4 * picture.get(x, down).getGreen() -
                picture.get(x, down2).getGreen()), 2);
            deltaB = Math.pow((-3 * picture.get(x, y).getBlue() + 4 * picture.get(x, down).getBlue() -
                picture.get(x, down2).getBlue()), 2);
        } else if (y == picture.height() - 1) {
            int up2 = y - 2;
            deltaR = Math.pow((-3 * picture.get(x, y).getRed() + 4 * picture.get(x, up).getRed() -
                picture.get(x, up2).getRed()), 2);
            deltaG = Math.pow((-3 * picture.get(x, y).getGreen() + 4 * picture.get(x, up).getGreen() -
                picture.get(x, up2).getGreen()), 2);
            deltaB = Math.pow((-3 * picture.get(x, y).getBlue() + 4 * picture.get(x, up).getBlue() -
                picture.get(x, up2).getBlue()), 2);
        } else {
            deltaR = Math.pow((picture.get(x, up).getRed() - picture.get(x, down).getRed()), 2);
            deltaG = Math.pow((picture.get(x, up).getGreen() - picture.get(x, down).getGreen()), 2);
            deltaB = Math.pow((picture.get(x, up).getBlue() - picture.get(x, down).getBlue()), 2);
        }


        return deltaR + deltaG + deltaB;


    }
}
