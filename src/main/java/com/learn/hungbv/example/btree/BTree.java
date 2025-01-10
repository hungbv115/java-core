package com.learn.hungbv.example.btree;
import com.learn.hungbv.annotation.Singleton;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

@Singleton
public class BTree<T extends Comparable<T>> implements Serializable {

    private static final int T = 2;  // Độ của cây B (bậc t)

    // Lớp đại diện cho một nút trong cây B
    private class Node implements Serializable {
        int n;  // Số lượng khóa trong nút
        T[] keys;  // Mảng khóa của kiểu T
        Node[] children;  // Mảng con trỏ tới các nút con
        boolean leaf;  // Xác định nút có phải là lá không

        public Node() {
            keys = (T[]) new Comparable[2 * T - 1];  // Khởi tạo mảng khóa
            children = (Node[]) Array.newInstance(Node.class, 2 * T);  // Mảng con
            this.n = 0;
            this.leaf = true;
        }
    }

    private Node root;  // Nút gốc của cây

    public BTree() {
        root = new Node();  // Khởi tạo cây B với nút gốc
    }

    // Hàm tìm kiếm khóa trong cây B
    public boolean search(T key) {
        return search(root, key) != null;
    }

    private Node search(Node node, T key) {
        int i = 0;
        while (i < node.n && key.compareTo(node.keys[i]) > 0) {
            i++;  // Tìm vị trí của khóa trong node
        }

        if (i < node.n && key.compareTo(node.keys[i]) == 0) {
            return node;  // Tìm thấy khóa trong node
        }

        if (!node.leaf) {
            return null;  // Nếu là nút lá và không tìm thấy khóa, trả về null
        }

        // Nếu không phải là nút lá, tiếp tục tìm kiếm trong các con
        return search(node.children[i], key);
    }

    // Chèn khóa vào cây B
    public void insert(T key) {
        Node root = this.root;
        if (root.n == 2 * T - 1) {
            Node newRoot = new Node();
            newRoot.children[0] = root;
            split(newRoot, 0);
            this.root = newRoot;  // Tạo root mới khi root đầy
        }
        insertNonFull(this.root, key);
    }

    private void insertNonFull(Node node, T key) {
        int i = node.n - 1;
        if (node.leaf) {
            // Tìm vị trí thích hợp để chèn khóa
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.n++;
        } else {
            // Tìm con thích hợp để chèn khóa vào
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                i--;
            }
            i++;
            if (node.children[i].n == 2 * T - 1) {
                split(node, i);
                if (key.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key);
        }
    }

    private void split(Node parent, int index) {
        Node fullNode = parent.children[index];
        Node newNode = new Node();
        newNode.leaf = fullNode.leaf;
        newNode.n = T - 1;

        // Chuyển các khóa từ fullNode sang newNode
        System.arraycopy(fullNode.keys, T, newNode.keys, 0, T - 1);

        // Nếu không phải là lá, chuyển các con từ fullNode sang newNode
        if (!fullNode.leaf) {
            System.arraycopy(fullNode.children, T, newNode.children, 0, T);
        }

        fullNode.n = T - 1;

        // Di chuyển khóa trung gian từ fullNode lên parent
        for (int i = parent.n; i > index; i--) {
            parent.children[i + 1] = parent.children[i];
        }
        parent.children[index + 1] = newNode;

        for (int i = parent.n - 1; i >= index; i--) {
            parent.keys[i + 1] = parent.keys[i];
        }
        parent.keys[index] = fullNode.keys[T - 1];
        parent.n++;
    }

    // In cây B để kiểm tra
    public void print() {
        print(root, 0);
    }

    private void print(Node node, int level) {
        if (node != null) {
            // In các khóa trong nút hiện tại
            System.out.print("Level " + level + ": ");
            for (int i = 0; i < node.n; i++) {
                System.out.print(node.keys[i] + " ");
            }
            System.out.println();

            // Nếu nút không phải là lá, in các nút con
            if (node.leaf) {
                for (int i = 0; i <= node.n; i++) {
                    print(node.children[i], level + 1);  // Đệ quy in các con
                }
            }
        }
    }

    public void printLevelOrder() {
        if (root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();

            // In ra các khóa của các nút trong một tầng
            while (size > 0) {
                Node currentNode = queue.poll();
                if(currentNode == null) {
                    size--;
                    continue;
                }
                System.out.print("[");
                for (int i = 0; i < currentNode.n; i++) {
                    System.out.print(currentNode.keys[i]);
                    if (i < currentNode.n - 1) System.out.print(", ");
                }

                System.out.print("] ");

                // Thêm các nút con vào hàng đợi
                if (currentNode.leaf) {
                    queue.addAll(Arrays.asList(currentNode.children).subList(0, currentNode.n + 1));
                }

                size--;
            }
            System.out.println();
        }
    }

    // Lưu cây B vào file
    public void saveToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);  // Ghi đối tượng cây B vào file
            System.out.println("Cây B đã được lưu vào tệp: " + fileName);
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu cây B vào file: " + e.getMessage());
        }
    }

    // Tải cây B từ file
    public static <T extends Comparable<T>> BTree<T> loadFromFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (BTree<T>) in.readObject();  // Đọc và phục hồi cây B từ file
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Lỗi khi tải cây B từ file: " + e.getMessage());
            return null;
        }
    }

//    @Schedule(time = 1)
    public static void tempMain() {
        // Tạo một cây B với kiểu String
        BTree<String> tree = new BTree<>();

        // Chèn một số phần tử vào cây
        tree.insert("Apple");
        tree.insert("Banana");
        tree.insert("Cherry");
        tree.insert("Date");
        tree.insert("Elderberry");
        tree.insert("Tlderberry");
        tree.insert("Modg");
        tree.insert("Flix");

        // Lưu cây vào file
        tree.saveToFile("btree.dat");

        tree.search("Apple");

        // Tải cây từ file
        BTree<String> loadedTree = BTree.loadFromFile("btree.dat");

        // In cây đã tải lên
        if (loadedTree != null) {
            loadedTree.print();
        }
    }
}
