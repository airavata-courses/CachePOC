package l2.poc;

import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import l2.poc.cache.Cache;
import l2.poc.cache.caffeine.CaffeineCache;
import l2.poc.utils.Store;

@Provider
public class DependencyInjectionBinder extends AbstractBinder {

 
	public DependencyInjectionBinder() {
	}

	@Override
	protected void configure() {
		bind(new CaffeineCache()).to(Cache.class);
		bind(new Store()).to(Store.class);
	}

}