package com.amituofo.common.kit.checksum;

import java.io.IOException;

public interface ChecksumGetter {

	Checksum getChecksum() throws IOException;

}
