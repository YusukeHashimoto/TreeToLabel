import java.util.*;
import java.util.stream.Collectors;

public class TreeToLabel {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Number of node: ");
		int num = sc.nextInt();
		//int num = 10;
		List<Node> tree = new ArrayList<>();
		for(int i = 0; i < num; i++) {
			tree.add(new Node(i));
		}
		
		do {
			System.out.println("Branch: ");
			int a = sc.nextInt();
			int b = sc.nextInt();
			if(a < 0) break;
			Branch branch = new Branch(a, b);
			tree.get(a).addBranch(branch);
			tree.get(b).addBranch(branch);
			System.out.println("{" + a + ", " + b + "}");
		} while(true);
		/*
		tree.get(0).addBranch(new Branch(0, 2));
		tree.get(2).addBranch(new Branch(0, 2));
		tree.get(2).addBranch(new Branch(7, 2));
		tree.get(7).addBranch(new Branch(7, 2));
		tree.get(1).addBranch(new Branch(1, 2));
		tree.get(2).addBranch(new Branch(1, 2));
		tree.get(1).addBranch(new Branch(1, 4));
		tree.get(4).addBranch(new Branch(1, 4));
		tree.get(3).addBranch(new Branch(3, 4));
		tree.get(4).addBranch(new Branch(3, 4));
		tree.get(3).addBranch(new Branch(3, 8));
		tree.get(8).addBranch(new Branch(3, 8));
		tree.get(4).addBranch(new Branch(4, 5));
		tree.get(5).addBranch(new Branch(4, 5));
		tree.get(5).addBranch(new Branch(5, 6));
		tree.get(6).addBranch(new Branch(5, 6));
		tree.get(6).addBranch(new Branch(6, 9));
		tree.get(9).addBranch(new Branch(6, 9));
		*/
		while(tree.stream().filter(n -> !n.hasRemoved()).count() > 2) {
			List<Node> nonLeafNodes = tree.stream().filter(node -> !node.isLeaf(tree) && !node.hasRemoved() && node.hasNextLeaf(tree)).collect(Collectors.toList());
			//List<Node> removeList = new ArrayList<>();
			nonLeafNodes.stream().forEach(n -> System.out.println(n.getId()));
			for(Node node: nonLeafNodes) {
				System.out.println("Node: " + node.getId());
				List<String> labels = 
				node.nextNode(tree).stream().filter(n -> n.isLeaf(tree) && !nonLeafNodes.contains(n)).map(leaf -> leaf.getLabel()).collect(Collectors.toList());
				String newLabel = "0";
				labels.add(node.getLabel().substring(1, node.getLabel().length()-1));
				Collections.sort(labels);
				for(String l : labels) { newLabel += l; }
				newLabel += "1";
				node.setLabel(newLabel);
				
				node.nextNode(tree).stream().filter(n -> n.isLeaf(tree) && !nonLeafNodes.contains(n)).forEach(l -> l.remove());
			}
			//removeList.stream().forEach(Node::remove);
			System.out.println();
		}
		
		List<Node> newtree = tree.stream().filter(n -> !n.hasRemoved()).collect(Collectors.toList());		
		if(newtree.size() == 1) System.out.println(newtree.get(0).getLabel());
		else {
			String a = newtree.get(0).getLabel();
			String b = newtree.get(1).getLabel();
			String label = a.compareTo(b) > 0 ? a + b : b + a;
			System.out.println(label);
		}
	}
}

class Node {
	private int id;
	private List<Branch> branches = new ArrayList<>();
	private String label = "01";
	private boolean hasRemoved = false;
	Node(int id) {
		this.id = id;
	}
	void addBranch(Branch branch) {
		branches.add(branch);
	}
	boolean isLeaf(List<Node> tree) {
		//return branches.size() == 1;
		return nextNode(tree).size() == 1;
	}
	List<Node> nextNode(List<Node> tree) {
		List<Node> nextNodes = new ArrayList<>();
		branches.stream().map(b -> tree.get(b.theOther(id))).filter(n -> !n.hasRemoved).forEach(n -> nextNodes.add(n));
		return nextNodes;
	}
	boolean hasNextLeaf(List<Node> tree) {
		return nextNode(tree).stream().anyMatch(m -> m.isLeaf(tree));
	}
	String getLabel() {
		return label;
	}
	void setLabel(String label) {
		this.label = label;
	}
	void remove() {
		System.out.println("Remove " + id);
		this.hasRemoved = true;
	}
	boolean hasRemoved() {
		return hasRemoved;
	}
	int getId() {
		return id;
	}
}

class Branch {
	private int a, b;
	Branch(int a, int b) {
		this.a = a;
		this.b = b;
	}
	int theOther(int id) {
		if(id == a) return b;
		else return a;
	}
}