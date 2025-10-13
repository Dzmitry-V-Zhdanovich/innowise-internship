package task1.customlinkedlist;

public class Main {
    public static void main(String[] args) {

        MyLinkedList<String> list = new MyLinkedList<>();
        list.add(0,"Audi");
        list.add(1,"BMW");
        list.add(2,"Mercedes");

        System.out.println("\nКолличество элементов: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        list.addFirst("VW");
        list.addLast("Toyota");
        System.out.println("\nКолличество элементов: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        System.out.println("\nУдалим последний элемент в списке: " + list.removeLast());
        System.out.println("Последним станет: " + list.getLast());

        System.out.println("\nУдалим первый: " + list.removeFirst());
        System.out.println("Первым элементом будет: " + list.getFirst());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        System.out.println("\nУдалим третий элемент: " + list.remove(2));
        System.out.println("Итоговый список: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}