import java.util.LinkedList;

public class HashTable {

	private final int hashTableSize = 1500;
	LinkedList<String>[] hashT;

	Dictionary[] hashTable;

	HashTable() {
		hashTable = new Dictionary[hashTableSize];
		for (int i = 0; i < hashTableSize; i++)
			hashTable[i] = null;
	}
	

	public String get(int hash) {
		if (hashTable[hash] == null)
			return null;
		else {
			Dictionary entry = hashTable[hash];
			while (entry != null && entry.getHash() != hash)
				entry = entry.getNext();

			if (entry == null)
				return null;
			else
				return entry.getValue();
		}
	}

	public void add(int hash, String word) {

		if (hashTable[hash] == null)
			hashTable[hash] = new Dictionary(hash, word);
		else {
			Dictionary entry = hashTable[hash];
			while (entry.getNext() != null && entry.getHash() == hash) {
				entry = entry.getNext();
			}
			entry.setNext(new Dictionary(hash, word));
		}
		
	}
	

	public void remove(int hash) {
		if (hashTable[hash] != null) {
			Dictionary prevValue = null;
			Dictionary value = hashTable[hash];
			while (value.getNext() != null && value.getHash() != hash) {
				prevValue = value;
				value = value.getNext();
			}
			if (value.getHash() == hash) {
				if (prevValue == null)
					hashTable[hash] = value.getNext();
				else
					prevValue.setNext(value.getNext());
			}
		}
	}


}