package com.amituofo.common.kit.io;

import java.io.IOException;
import java.io.InputStream;

public interface NextInputStream {
	InputStream next() throws IOException;
}