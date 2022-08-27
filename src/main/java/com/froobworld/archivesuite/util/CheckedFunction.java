package com.froobworld.archivesuite.util;

import java.io.IOException;

public interface CheckedFunction<T, R> {

    R apply(T t) throws IOException;

}
