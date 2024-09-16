package com.mezocode.healthcare.shared.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Setter
@Getter
public class ListRestResponse<T extends Responseable> implements RestResponse<T> {

    private final List<?> data;

    private ListRestResponse(@NotNull List<? extends Responseable> data) {
        this.data = data;
    }

    public static ListRestResponse<Responseable> responseBody(@NotNull List<? extends Responseable> data) {
        return new ListRestResponse<>(data);
    }
}
