package com.froobworld.archivesuite.util;

import java.io.IOException;

public interface CheckedBiFunction<T, U, R> {

    R apply(T t, U u) throws IOException;

}
