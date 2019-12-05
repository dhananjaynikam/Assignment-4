package sample;

public class CircularList {
    public Node head = null;
    public Node tail = null;
    private int size = 0;
    public void add(PositionId data){
        Node newNode = new Node(data);
        if(head == null) {
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.index = 0;
            size++;
        }
        else {
            tail.next = newNode;
            newNode.index = size;
            tail = newNode;
            tail.next = head;
            size++;
        }
    }

    public int size(){
        return size;
    }

    public PositionId get(int index){
        PositionId item = new PositionId(0,0);
        Node itr = head;
        while(itr != tail){
            if(itr.index ==  index){
                item = itr.data;
                break;
            }
            itr = itr.next;
        }
        return item;
    }
}
