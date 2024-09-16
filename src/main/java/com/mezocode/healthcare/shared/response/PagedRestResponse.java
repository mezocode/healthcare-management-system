package com.mezocode.healthcare.shared.response;

import java.util.HashMap;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Setter
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagedRestResponse<T extends Responseable> implements RestResponse<T> {

    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String TOTAL_ELEMENTS = "total_elements";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String LAST_PAGE = "last_page";
    private List<? extends Responseable> data;
    private HashMap<String, Object> meta;


    private PagedRestResponse(@NonNull Page<? extends Responseable> pageable) {
        data = pageable.getContent();
        meta = new HashMap<>();
        meta.put(PAGE, pageable.getNumber() + 1);
        meta.put(SIZE, pageable.getSize());
        meta.put(TOTAL_ELEMENTS, pageable.getTotalElements());
        meta.put(TOTAL_PAGES, pageable.getTotalPages());
        meta.put(LAST_PAGE, pageable.isLast());
    }

    public static PagedRestResponse<Responseable> responseBody(@NonNull Page<? extends Responseable> pageable) {
        return new PagedRestResponse<>(pageable);
    }
}
