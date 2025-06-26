package io;

import java.lang.reflect.Parameter;

public interface OutputRenderer
{
    void print(String message);
    void println(String message);
    void printf(String message, Object... args);
}
