package com.example.demo.vo;

import java.security.Principal;
import java.util.Objects;

public class ExchangePrincipal implements Principal {

	private String name;

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object another) {
        if(!(another instanceof Principal)) {
        	return false;
        }
        Principal principal = (Principal)another;
        return principal.getName() == this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
