package com.froobworld.archivesuite.util;

import java.io.IOException;

public interface CheckedBiConsumer<T, U> {

    void accept(T t, U u) throws IOException;

}
