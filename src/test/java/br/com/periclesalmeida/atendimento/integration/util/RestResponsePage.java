package br.com.periclesalmeida.atendimento.integration.util;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class RestResponsePage<T> implements Serializable {

    List<T> content;
    Pageable pageable; long total;

    public RestResponsePage(List<T> content) {
        this.content = content;
        this.pageable = pageable;
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

