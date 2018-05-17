package org.nianet.plexil.maude2java;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

//import java.util.Iterator;

/**
 * 
 * @author Hector Fabio Cadavid Rengifo
 *
 */
public class ObservableBufferAdapter<T> extends LinkedList<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<T> list;
	private OutputMonitor outmon;
	
	public ObservableBufferAdapter(LinkedList<T> list){
		this.list=list;
		outmon=new OutputMonitor();				
	}
	
	public void addObserver(Observer o){
		outmon.addObserver(o);
	}
	
	

	@Override
	public void add(int index, T element) {
		// TODO Auto-generated method stub
		list.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return list.addAll(index, c);
	}

	@Override
	public void addFirst(T e) {
		// TODO Auto-generated method stub
		list.addFirst(e);
	}

	@Override
	public void addLast(T e) {
		// TODO Auto-generated method stub
		list.addLast(e);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		list.clear();
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return list.clone();
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return list.contains(o);
	}

//	public Iterator<T> descendingIterator() {
//		// TODO Auto-generated method stub
//		return list.descendingIterator();
//	}

	@Override
	public T element() {
		// TODO Auto-generated method stub
		return list.element();
	}

	@Override
	public T get(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	@Override
	public T getFirst() {
		// TODO Auto-generated method stub
		return list.getFirst();
	}

	@Override
	public T getLast() {
		// TODO Auto-generated method stub
		return list.getLast();
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		// TODO Auto-generated method stub
		return list.listIterator(index);
	}

	@Override
	public boolean offer(T e) {
		// TODO Auto-generated method stub
		return list.offer(e);
	}

//	public boolean offerFirst(T e) {
//		// TODO Auto-generated method stub
//		return list.offerFirst(e);
//	}

//	public boolean offerLast(T e) {
//		// TODO Auto-generated method stub
//		return list.offerLast(e);
//	}

	@Override
	public T peek() {
		// TODO Auto-generated method stub
		return list.peek();
	}

//	public T peekFirst() {
//		// TODO Auto-generated method stub
//		return list.peekFirst();
//	}

//	public T peekLast() {
//		// TODO Auto-generated method stub
//		return list.peekLast();
//	}

	@Override
	public T poll() {
		// TODO Auto-generated method stub
		return list.poll();
	}

//	public T pollFirst() {
//		// TODO Auto-generated method stub
//		return list.pollFirst();
//	}

//	public T pollLast() {
//		// TODO Auto-generated method stub
//		return list.pollLast();
//	}

//	public T pop() {
//		// TODO Auto-generated method stub
//		return list.pop();
//	}

//	public void push(T e) {
//		// TODO Auto-generated method stub
//		list.push(e);
//	}

	@Override
	public T remove() {
		// TODO Auto-generated method stub
		return list.remove();
	}

	@Override
	public T remove(int index) {
		// TODO Auto-generated method stub
		return list.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return list.remove(o);
	}

	@Override
	public T removeFirst() {
		// TODO Auto-generated method stub
		return list.removeFirst();
	}

//	@Override
//	public boolean removeFirstOccurrence(Object o) {
//		// TODO Auto-generated method stub
//		return list.removeFirstOccurrence(o);
//	}

	@Override
	public T removeLast() {
		// TODO Auto-generated method stub
		return list.removeLast();
	}

//	@Override
//	public boolean removeLastOccurrence(Object o) {
//		// TODO Auto-generated method stub
//		return list.removeLastOccurrence(o);
//	}

	@Override
	public T set(int index, T element) {
		// TODO Auto-generated method stub
		return list.set(index, element);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return list.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return list.toArray(a);
	}

	
	@Override
	public boolean add(T e) {
		outmon.newOutput(e);
		return list.add(e);
	}		
	
}


class ObservableAdapter<T> extends Observable{
	private ObservableBufferAdapter<T> observable;
	
	public ObservableAdapter(ObservableBufferAdapter<T> obs){
		this.observable=obs;
	}
	
	public ObservableBufferAdapter<T> getRealObservable(){
		return observable;
	}
		
	
}
