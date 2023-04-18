package com.olonge.demos.services.nums;

public class SampleAbstractClassImpl extends SampleAbstractClass {
    @Override
    public int callPublic(long param) {
        return 0;
    }

    @Override
    protected int callProtected(long param) {
        return 0;
    }
}
