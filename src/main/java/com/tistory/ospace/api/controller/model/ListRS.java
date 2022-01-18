package com.tistory.ospace.api.controller.model;

import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tistory.ospace.api.util.BaseUtils;
import com.tistory.ospace.common.BaseModel;
import com.tistory.ospace.common.DataUtils;

@JsonInclude(Include.NON_NULL)
public class ListRS<T> extends BaseModel {
	private long total;
	private List<T> data;
	  
	public static <T> ListRS<T> of(List<T> data, long total) {
		ListRS<T> ret = new ListRS<T>();
		ret.setData(data);
		ret.setTotal(total);
		
		return ret;
	}
	
	public static <T, R> ListRS<R> of(List<T> data, Function<T, R> converter) {
	    long total = BaseUtils.getTotal(data);
	    
	    return of(DataUtils.map(data, converter), total);
    }

    public long getTotal() {
      return total;
    }

    public void setTotal(long total) {
      this.total = total;
    }

    public List<T> getData() {
      return data;
    }

    public void setData(List<T> data) {
      this.data = data;
    }
}
