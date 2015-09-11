package farto.cleva.guilherme.totvs.vo.framework;

import java.util.UUID;

public abstract class GenericVO {

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

}
