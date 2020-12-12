### Hashtable简介

和HashMap一样，Hashtable也是一个散列表，它存储的内容是键值对(key-value)映射。Hashtable继承于Dictionary，实现了Map、Cloneable、java.io.Serializable接口。

**Hashtable的函数都是同步的，这意味着它是线程安全的。它的key、value都不可以为null（为null时将抛出NullPointerException）。此外，Hashtable中的映射不是有序的。**

此类实现一个哈希表，该哈希表将键映射到相应的值。任何非null对象都可以用作键或值。为了成功地在哈希表中存储和获取对象，用作键的对象必须实现 hashCode 方法和 equals 方法。

Hashtable 的实例有两个参数影响其性能：初始容量 和 加载因子。容量 是哈希表中桶的数量，初始容量 就是哈希表创建时的容量。注意，哈希表的状态为 open：在发生“哈希冲突”的情况下，单个桶会存储多个条目，这些条目必须按顺序搜索。加载因子 是对哈希表在其容量自动增加之前可以达到多满的一个尺度。初始容量和加载因子这两个参数只是对该实现的提示。关于何时以及是否调用 rehash 方法的具体细节则依赖于该实现。
通常，默认加载因子是 0.75, 这是在时间和空间成本上寻求一种折衷。加载因子过高虽然减少了空间开销，但同时也增加了查找某个条目的时间（在大多数 Hashtable 操作中，包括 get 和 put 操作，都反映了这一点）。

### Hashtable构造函数

- 默认构造函数，容量为11，加载因子为0.75。

    

    ```cpp
      public Hashtable() {
          this(11, 0.75f);
      }
    ```

- 用指定初始容量和默认的加载因子 (0.75) 构造一个新的空哈希表。

    

    ```cpp
      public Hashtable(int initialCapacity) {
          this(initialCapacity, 0.75f);
      }
    ```

- 用指定初始容量和指定加载因子构造一个新的空哈希表。

    

    ```cpp
      public Hashtable(int initialCapacity, float loadFactor) {
          if (initialCapacity < 0)
              throw new IllegalArgumentException("Illegal Capacity: "+
                                                 initialCapacity);
          if (loadFactor <= 0 || Float.isNaN(loadFactor))
              throw new IllegalArgumentException("Illegal Load: "+loadFactor);
    
          if (initialCapacity==0)
              initialCapacity = 1;
          this.loadFactor = loadFactor;
          table = new Entry[initialCapacity];
          threshold = (int)Math.min(initialCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
          initHashSeedAsNeeded(initialCapacity);
      }
    ```

    其中initHashSeedAsNeeded方法用于初始化hashSeed参数，其中hashSeed用于计算key的hash值，它与key的hashCode进行按位异或运算。这个hashSeed是一个与实例相关的随机值，主要用于解决hash冲突。

    

    ```cpp
      private int hash(Object k) {
          // hashSeed will be zero if alternative hashing is disabled.
          return hashSeed ^ k.hashCode();
      }
    ```

- 构造一个与给定的 Map 具有相同映射关系的新哈希表。

    

    ```tsx
      public Hashtable(Map<? extends K, ? extends V> t) {
          //设置table容器大小，其值==t.size * 2 + 1
          this(Math.max(2*t.size(), 11), 0.75f);
          putAll(t);
      }
    ```

### Hashtable继承关系



```java
public class Hashtable<K,V>
extends Dictionary<K,V>
implements Map<K,V>, Cloneable, java.io.Serializable
```

![img](https://upload-images.jianshu.io/upload_images/2834970-8e8afb5294de5f43.png?imageMogr2/auto-orient/strip|imageView2/2/w/463/format/webp)

image.png

从图中可以看出：

- Hashtable继承于Dictionary类，实现了Map接口。Map是"key-value键值对"接口，Dictionary是声明了操作"键值对"函数接口的抽象类。
- Hashtable是通过"拉链法"实现的哈希表。它包括几个重要的成员变量：table，count，threshold，loadFactor，modCount。
- table是一个Entry[]数组类型，而Entry实际上就是一个单向链表。哈希表的“key-value键值对”都是存储在Entry数组中的。
- count是Hashtable的大小，它是Hashtable保存的键值对的数量。
- threshold是Hashtable的阈值，用于判断是否需要调整Hashtable的容量。threshold的值=“容量*加载因子“。
- loadFactor就是加载因子。
- modCount是用来实现fail-fast机制的

### Hashtable方法

- put

将指定key映射到此哈希表中的指定value。注意这里键key和值value都不可为null。



```csharp
public synchronized V put(K key, V value) {
    // value为null则抛NullPointerException
    if (value == null) {
        throw new NullPointerException();
    }

    /*
     * 确保key在table[]是不重复的
     * 处理过程：
     * 1、计算key的hash值，确认在table[]中的索引位置
     * 2、迭代index索引位置，如果该位置处的链表中存在一个一样的key，则替换其value，返回旧值
     */
    Entry tab[] = table;
    //计算key的hash值
    int hash = hash(key);
    //确认该key的索引位置
    int index = (hash & 0x7FFFFFFF) % tab.length;
    //迭代，寻找该key，替换
    for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            V old = e.value;
            e.value = value;
            return old;
        }
    }

    modCount++;
    //如果容器中的元素数量已经达到阀值，则进行扩容操作
    if (count >= threshold) {
        // Rehash the table if the threshold is exceeded
        rehash();

        tab = table;
        hash = hash(key);

        index = (hash & 0x7FFFFFFF) % tab.length;
    }

    // Creates the new entry.
    Entry<K,V> e = tab[index];
    tab[index] = new Entry<>(hash, key, value, e);
    count++;
    return null;
}
```

put方法的整个处理流程是：计算key的hash值，根据hash值获得key在table数组中的索引位置，然后迭代该key处的Entry链表（我们暂且理解为链表），若该链表中存在一个这个的key对象，那么就直接替换其value值即可，否则在将该key-value节点插入该index索引位置处。如下：

首先我们假设一个容量为5的table，存在8、10、13、16、17、21。他们在table中位置如下：

![img](https://upload-images.jianshu.io/upload_images/2834970-9e6ec019d9314708.png?imageMogr2/auto-orient/strip|imageView2/2/w/360/format/webp)

image.png

然后我们插入一个数：put(16,22)，key=16在table的索引位置为1，同时在1索引位置有两个数，程序对该“链表”进行迭代，发现存在一个key=16，这时要做的工作就是用newValue=22替换oldValue=16，并将oldValue=16返回。

![img](https://upload-images.jianshu.io/upload_images/2834970-598d36715b522abc.png?imageMogr2/auto-orient/strip|imageView2/2/w/375/format/webp)

image.png

在put(31,31)，key=31所在的索引位置为3，并且在该链表中也没有存在某个key=31的节点，所以就将该节点插入该链表的第一个位置。

![img](https://upload-images.jianshu.io/upload_images/2834970-18a0750662893296.png?imageMogr2/auto-orient/strip|imageView2/2/w/409/format/webp)

image.png

在HashTabled的put方法中有两个地方需要注意：

1. HashTable的扩容操作。在put方法中，如果需要向table[]中添加Entry元素，会首先进行容量校验，如果容量已经达到了阀值，HashTable就会进行扩容处理rehash()，如下：

    

    ```java
     protected void rehash() {
         int oldCapacity = table.length;
         Entry<K,V>[] oldMap = table;
    
         // overflow-conscious code
         int newCapacity = (oldCapacity << 1) + 1;
         if (newCapacity - MAX_ARRAY_SIZE > 0) {
             if (oldCapacity == MAX_ARRAY_SIZE)
                 // Keep running with MAX_ARRAY_SIZE buckets
                 return;
             newCapacity = MAX_ARRAY_SIZE;
         }
         //新建一个size = newCapacity 的HashTable
         Entry<K,V>[] newMap = new Entry[newCapacity];
    
         modCount++;
         //重新计算阀值
         threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
         //重新计算hashSeed
         boolean rehash = initHashSeedAsNeeded(newCapacity);
    
         table = newMap;
         //将原来的元素拷贝到新的HashTable中
         for (int i = oldCapacity ; i-- > 0 ;) {
             for (Entry<K,V> old = oldMap[i] ; old != null ; ) {
                 Entry<K,V> e = old;
                 old = old.next;
    
                 if (rehash) {
                     e.hash = hash(e.key);
                 }
                 int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                 e.next = newMap[index];
                 newMap[index] = e;
             }
         }
     }
    ```

在这个rehash()方法中我们可以看到**容量扩大两倍+1**，同时需要将原来HashTable中的元素一一复制到新的HashTable中，这个过程是比较消耗时间的，同时还需要重新计算hashSeed的，毕竟容量已经变了。

1. 在计算索引位置index时，HashTable进行了一个与运算过程（hash & 0x7FFFFFFF），至于为什么要与 0x7FFFFFFF，那是hashtable 提供的hash算法，hashMap提供了不同的算法，用户如果要定义自己的算法也是可以的。

    

    ```cpp
     private int hash(Object k) {
             return hashSeed ^ k.hashCode();
     }
    ```



- get

相对于put方法，get方法就会比较简单，处理过程就是计算key的hash值，判断在table数组中的索引位置，然后迭代链表，直到找到匹配相应key的value，若没有找到返回null。



```csharp
public synchronized V get(Object key) {
    Entry tab[] = table;
    int hash = hash(key);
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            return e.value;
        }
    }
    return null;
}
```

- putAll

putAll的作用是将“Map(t)”的全部元素逐一添加到Hashtable中。



```dart
public synchronized void putAll(Map<? extends K, ? extends V> t) {
    for (Map.Entry<? extends K, ? extends V> e : t.entrySet())
        put(e.getKey(), e.getValue());
}
```

- clear

clear() 的作用是清空Hashtable。它是将Hashtable的table数组的值全部设为null



```java
public synchronized void clear() {
    Entry tab[] = table;
    modCount++;
    for (int index = tab.length; --index >= 0; )
        tab[index] = null;
    count = 0;
}
```

- contains和 containsValue

contains() 和 containsValue() 的作用都是判断Hashtable是否包含“值(value)”



```csharp
public synchronized boolean contains(Object value) {
    if (value == null) {
        throw new NullPointerException();
    }

    Entry tab[] = table;
    for (int i = tab.length ; i-- > 0 ;) {
        for (Entry<K,V> e = tab[i] ; e != null ; e = e.next) {
            if (e.value.equals(value)) {
                return true;
            }
        }
    }
    return false;
}

public boolean containsValue(Object value) {
    return contains(value);
}
```

- containsKey

containsKey() 的作用是判断Hashtable是否包含key



```java
public synchronized boolean containsKey(Object key) {
    Entry tab[] = table;
    int hash = hash(key);
    // 计算索引值，% tab.length 的目的是防止数据越界
    int index = (hash & 0x7FFFFFFF) % tab.length;
    // 找到“key对应的Entry(链表)”，然后在链表中找出“哈希值”和“键值”与key都相等的元素
    for (Entry<K,V> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            return true;
        }
    }
    return false;
}
```

- remove

remove() 的作用就是删除Hashtable中键为key的元素



```csharp
public synchronized V remove(Object key) {
    Entry tab[] = table;
    int hash = hash(key);
    int index = (hash & 0x7FFFFFFF) % tab.length;
    // 找到“key对应的Entry(链表)”，然后在链表中找出要删除的节点，并删除该节点。
    for (Entry<K,V> e = tab[index], prev = null ; e != null ; prev = e, e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            modCount++;
            if (prev != null) {
                prev.next = e.next;
            } else {
                tab[index] = e.next;
            }
            count--;
            V oldValue = e.value;
            e.value = null;
            return oldValue;
        }
    }
    return null;
}
```

