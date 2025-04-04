import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class KDTree {
	int depth;
	double[] point;
	KDTree left;
	KDTree right;

	KDTree(double[] point, int depth) {
		this.point = point;
		this.depth = depth;
	}

	boolean compare(double[] a) {
		int r = this.depth % a.length;
		if (a[r] >= this.point[r])
			return true;
		return false;
	}

	static KDTree insert(KDTree tree, double[] p) {
		if (tree == null)
			return new KDTree(p, 0);

		KDTree current = tree;
		KDTree parent = null;
		int depth = 0;

		while (current != null) {
			parent = current;
			int axis = depth % p.length;
			if (p[axis] >= current.point[axis]) {
				current = current.right;
			} else {
				current = current.left;
			}
			depth++;
		}

		KDTree newNode = new KDTree(p, depth);
		int axis = (depth - 1) % p.length;
		if (p[axis] >= parent.point[axis]) {
			parent.right = newNode;
		} else {
			parent.left = newNode;
		}

		return tree;
	}

	static double sqDist(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			double d = a[i] - b[i];
			sum += d * d;
		}
		return sum;
	}

	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		double d = sqDist(a, champion);
		double d2 = sqDist(a, tree.point);
		if (d2 <= d) {
			champion = tree.point;
			d = d2;
		}
		if (tree.left != null) {
			double[] leftChampion = closestNaive(tree.left, a, champion);
			d2 = sqDist(a, leftChampion);
			if (d2 <= d) {
				champion = leftChampion;
				d = d2;
			}
		}
		if (tree.right != null) {
			double[] rightChampion = closestNaive(tree.right, a, champion);
			d2 = sqDist(a, rightChampion);
			if (d2 <= d) {
				champion = rightChampion;
			}
		}
		return champion;
	}

	static double[] closestNaive(KDTree tree, double[] a) {
		if (tree == null)
			return null;

		double[] champion = tree.point;
		return closestNaive(tree, a, champion);
	}

	static double[] closest(KDTree tree, double[] a, double[] champion) {
		if (tree == null)
			return champion;

		// sert pour InteractiveClosest.
		InteractiveClosest.trace(tree.point, champion);

		double d = sqDist(a, champion);
		double d2 = sqDist(a, tree.point);
		if (d2 <= d) {
			champion = tree.point;
			d = d2;
		}

		int idx = tree.depth % a.length;
		KDTree near, far;
		if (a[idx] < tree.point[idx]) {
			near = tree.left;
			far = tree.right;
		} else {
			near = tree.right;
			far = tree.left;
		}

		double[] nearChampion = closest(near, a, champion);
		d2 = sqDist(a, nearChampion);
		if (d2 <= d) {
			champion = nearChampion;
			d = d2;
		}

		double sqPlaneDist = (tree.point[idx] - a[idx]) * (tree.point[idx] - a[idx]);
		if (sqPlaneDist < d) {
			double[] farChampion = closest(far, a, champion);
			d2 = sqDist(a, farChampion);
			if (d2 <= d) {
				champion = farChampion;
			}
		}

		return champion;
	}

	static double[] closest(KDTree tree, double[] a) {
		if (tree == null)
			return null;
		double[] champion = tree.point;
		return closest(tree, a, champion);
	}

	static int size(KDTree tree) {
		if (tree == null) {
			return 0;
		}
		Stack<KDTree> stack = new Stack<>();
		stack.push(tree);
		int size = 0;
		while (!stack.isEmpty()) {
			KDTree current = stack.pop();
			size++;
			if (current.left != null) {
				stack.push(current.left);
			}
			if (current.right != null) {
				stack.push(current.right);
			}
		}
		return size;
	}

	static void sum(KDTree tree, double[] acc) {
		if (tree == null) {
			return;
		}
		Stack<KDTree> stack = new Stack<>();
		stack.push(tree);

		while (!stack.isEmpty()) {
			KDTree current = stack.pop();
			for (int i = 0; i < acc.length; i++) {
				acc[i] += current.point[i];
			}
			if (current.right != null) {
				stack.push(current.right);
			}
			if (current.left != null) {
				stack.push(current.left);
			}
		}
	}

	static double[] average(KDTree tree) {
		double[] points = new double[tree.point.length];
		int _size = size(tree);
		sum(tree, points);
		for (int i = 0; i < points.length; i++) {
			points[i] /= _size;
		}
		return points;
	}

	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		Vector<double[]> palette = new Vector<>();
		if (tree == null)
			return palette;

		Queue<KDTree> queue = new LinkedList<>();
		queue.add(tree);

		List<KDTree> subtrees = new ArrayList<>();
		while (!queue.isEmpty() && subtrees.size() < maxpoints) {
			KDTree current = queue.poll();
			subtrees.add(current);
			if (current.left != null) {
				queue.add(current.left);
			}
			if (current.right != null) {
				queue.add(current.right);
			}
		}

		for (KDTree subtree : subtrees) {
			double[] avg = average(subtree);
			palette.add(avg);
		}
		return palette;
	}

	public String pointToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (this.point.length > 0)
			sb.append(this.point[0]);
		for (int i = 1; i < this.point.length; i++)
			sb.append("," + this.point[i]);
		sb.append("]");
		return sb.toString();
	}

}
