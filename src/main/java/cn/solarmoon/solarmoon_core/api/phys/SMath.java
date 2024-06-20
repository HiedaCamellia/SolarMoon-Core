package cn.solarmoon.solarmoon_core.api.phys;

public class SMath {

    /**
     * 二次差值
     * @param P1 控制点，将决定曲线的过渡重心，比如从0-10中，p1为1，则过渡先慢后快
     */
    public static float quadraticInterpolation(float t, float P0, float P1, float P2) {
        return (1 - t) * (1 - t) * P0 + 2 * (1 - t) * t * P1 + t * t * P2;
    }

    /**
     * 平滑差值
     * @param n 平滑度
     */
    public static float smoothInterpolation(float progress, float start, float end, float n) {
        float t = 1 - (float)Math.pow(1 - progress, n);
        return start + t * (end - start);
    }

    /**
     * 正弦差值
     */
    public static float sineInterpolation(float t, float P0, float P1) {
        return P0 + (P1 - P0) * (1 - (float)Math.cos(t * Math.PI)) / 2;
    }

    /**
     * 余弦差值
     */
    public static float cosineInterpolation(float t, float P0, float P1) {
        float ft = (float) (t * Math.PI);
        float f = (1 - (float)Math.cos(ft)) * 0.5f;
        return  P0 * (1-f) + P1 * f;
    }

    /**
     * 抛物线函数
     * @param x 当前x值（当前点所在抛物线的位置）
     * @param vertexX 顶点x坐标
     * @param vertexY 顶点y坐标
     * @param initialY 初始y坐标
     * @return 当前x值所对应点的y值
     */
    public static double parabolaFunction(double x, double vertexX, double vertexY, double initialY) {
        // a 是抛物线的开口方向和宽度，由顶点和初始值决定
        double a = (initialY - vertexY) / Math.pow((0 - vertexX), 2);
        // 使用抛物线方程 y = a*(x-h)^2 + k
        return a * Math.pow((x - vertexX), 2) + vertexY;
    }

}
