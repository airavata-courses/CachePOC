package l2.poc;

import java.net.URI;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class DefaultLoggerContextFactory implements LoggerContextFactory {
	private LoggerContext loggerContext;

	public DefaultLoggerContextFactory(LoggerContext loggerContext) {
		this.loggerContext=loggerContext;
	}

	@Override
	public LoggerContext getContext(String arg0, ClassLoader arg1, Object arg2, boolean arg3) {
		return loggerContext;
	}

	@Override
	public LoggerContext getContext(String arg0, ClassLoader arg1, Object arg2, boolean arg3, URI arg4, String arg5) {
		return loggerContext;
	}

	@Override
	public void removeContext(LoggerContext arg0) {

	}

}
