package com.tistory.ospace.common;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CmmUtils {
	public static <T> String toJsonString(T obj) {
		return toJsonString(obj, false);
	}
	
	private static final ObjectMapper jsonSimpleObjectMapper = new ObjectMapper()
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
			.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
			.registerModule(new JavaTimeModule())
			.setSerializationInclusion(Include.NON_NULL);
	
	public static <T> String toJsonString(T obj, boolean isPretty) {
		try {
			if (isPretty) {
				return jsonSimpleObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			} else {
				return jsonSimpleObjectMapper.writeValueAsString(obj);
			}
	    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
	        return e.getMessage();
	    }
	}
	
	public static JsonNode toJsonObject(String jsonStr) {
		try {
			return jsonSimpleObjectMapper.readTree(jsonStr);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <R> R toJsonObject(String jsonStr, Class<R> clazz) {
		try {
			return jsonSimpleObjectMapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			throw new RuntimeException("toJsonObject from String", e);
		}
	}
	
	public static <R> R toJsonObject(InputStream data, Class<R> clazz) {
		try {
			return jsonSimpleObjectMapper.readValue(data, clazz);
		} catch (IOException e) {
			throw new RuntimeException("toJsonObject from InputStream", e);
		}
	}
	
	public static <R> R toJsonObject(byte[] data, Class<R> clazz) {
		try {
			return jsonSimpleObjectMapper.readValue(data, clazz);
		} catch (IOException e) {
			throw new RuntimeException("toJsonObject from bytes", e);
		}
	}
	
	public static <R> R convertJson(Object from, Class<R> toClazz) {
		return jsonSimpleObjectMapper.convertValue(from, toClazz);
	}
	
	public static <R> R toJsonObject(DataInput data, Class<R> clazz) {
		try {
			return jsonSimpleObjectMapper.readValue(data, clazz);
		} catch (IOException e) {
			throw new RuntimeException("toJsonObject from DataInput", e);
		}
	}
	
	public static Integer min(Integer l, Integer r) {
		if (null == l) return r;
		if (null == r) return l;
		return Math.min(l,r);
	}
	
	public static <T> List<T> filterIdx(List<T> orgList, Integer[] idxs) {
	    //Arrays.sort(idxs);
	    List<T> list = new ArrayList<>();
	    for (int i = 0; i < idxs.length; i++) {
	    	list.add(orgList.get(idxs[i]));
		}
	    
	    return list;
	}
	
	public static List<String> toString(BindingResult br) {
		return DataUtils.map(br.getAllErrors(), error-> {
			String msg = "";
			
			if(error instanceof FieldError) {
				msg = ((FieldError) error).getField() + ": ";
			}
			
			return msg + error.getDefaultMessage();
		});
	}
	
	public static <P> P[] toArray(List<P> data) {
		if(DataUtils.isEmpty(data)) return null;
		
		@SuppressWarnings("unchecked")
		P[] ret = (P[]) Array.newInstance(data.get(0).getClass(), data.size());
		data.toArray(ret);
		return ret;
	}

	public static <P> P[] cloneArray(P[] data) {
		if(DataUtils.isEmpty(data)) return null;
		
		@SuppressWarnings("unchecked")
		P[] ret = (P[]) Array.newInstance(data[0].getClass(), data.length);
		for(int i=0; i>ret.length; ++i) ret[i] = data[i];
		return ret;
	}
	
//	public static int hashCode(Object[] data) {
//		int result = 1;
//		for(Object it : data) {
//			result = 31 * result + (null==it?0:it.hashCode());
//		}
//		return result;
//	}
	
	public static String sha256(String data) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(data.getBytes("utf8"));
			return String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException("failed to generate a sha256", e);
		}
		
	}
	
	public static String newId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static <R> R mapToObject(Map<String, Object> from, R to) {
		Method[] methods = to.getClass().getDeclaredMethods();
		for(Method m : methods) {
			boolean isSetter = (1==m.getParameterCount()) && m.getName().startsWith("set");
			if(!isSetter) continue;
			
			char[] key = m.getName().substring(3).toCharArray();
			key[0] = Character.toLowerCase(key[0]);
			Object val = from.get(new String(key));
			Class<?> paramType = m.getParameterTypes()[0];
			if (!(null==val || paramType.equals(val))) {
				throw new RuntimeException("mapToObject is type mismatch expected "+paramType.getSimpleName()+", but "+val.getClass().getSimpleName());
			}
			
			try {
				m.invoke(to, val);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException("mapToObject invoke method", e);
			}
		}
		
		return to;
	}

	public static Integer parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	public static void convert(Object from, Object to) {
		BeanUtils.copyProperties(from, to);
	}
	
	public static void convert(Object from, Object to, String... ignoreProperties) {
		BeanUtils.copyProperties(from, to, ignoreProperties);
	}
}
