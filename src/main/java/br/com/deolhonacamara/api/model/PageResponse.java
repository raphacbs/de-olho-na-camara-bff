package br.com.deolhonacamara.api.model;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Function;

@AllArgsConstructor
public class PageResponse<T> implements Page<T> {

    private final List<T> content;
    private final int totalElements;
    private final int number;
    private final int size;
    private final Sort sort;

    @Override
    public int getTotalPages() {
        return this.size == 0 ? 1 : (int) Math.ceil((double) this.totalElements / (double) this.size);
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public boolean isFirst() {
        return number == 0;
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return number + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return number > 0;
    }

    @Override
    public Pageable nextPageable() {
        if (!hasNext()) return Pageable.unpaged();
        return PageRequest.of(number + 1, size, sort);
    }

    @Override
    public Pageable previousPageable() {
        if (!hasPrevious()) return Pageable.unpaged();
        return PageRequest.of(number - 1, size, sort);
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> converted = new ArrayList<>(content.size());
        for (T element : content) {
            converted.add(converter.apply(element));
        }
        return new PageResponse<>(converted, totalElements, number, size, sort);
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
