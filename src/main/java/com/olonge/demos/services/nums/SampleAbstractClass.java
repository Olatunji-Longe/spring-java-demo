package com.olonge.demos.services.nums;

public abstract class SampleAbstractClass {

    public abstract int callPublic(long param);

    protected abstract int callProtected(long param);


    private String callPrivate(long param) {
        //Some other logic here that needs to be tested
        if(callPublic(param) == 0){
            return String.format("public: %s", callPublic(param));
        }
        if(callPublic(param) == 1){
            return String.format("protected: %s", callProtected(param));
        }
        return "none";
    }

    public String call(long param) {
        return callPrivate(param);
    }
}
