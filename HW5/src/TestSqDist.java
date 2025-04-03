public class TestSqDist {

	public static void main(String[] args) {
		// Test case 1: 2D points
		double[] point1 = { -1, 1 };
		double[] point2 = { 1, -1 };
		double expected1 = 8.0;
		double result1 = KDTree.sqDist(point1, point2);
		assert result1 == expected1 : "Test 1 Failed";

		// Test case 2: 1D points
		double[] point3 = { 3 };
		double[] point4 = { 7 };
		double expected2 = 16.0;
		double result2 = KDTree.sqDist(point3, point4);
		assert result2 == expected2 : "Test 2 Failed";

		// Test case 3: 3D points
		double[] point5 = { 1, 2, 3 };
		double[] point6 = { 4, 5, 6 };
		double expected3 = 27.0;
		double result3 = KDTree.sqDist(point5, point6);
		assert result3 == expected3 : "Test 3 Failed";

		// Test case 4: Zero-dimensional points
		double[] point7 = {};
		double[] point8 = {};
		double expected4 = 0.0;
		double result4 = KDTree.sqDist(point7, point8);
		assert result4 == expected4 : "Test 4 Failed";

		System.out.println("[OK]");
	}
}