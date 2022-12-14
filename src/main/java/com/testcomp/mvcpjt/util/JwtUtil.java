package com.testcomp.mvcpjt.util;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

import com.testcomp.mvcpjt.util.db.UserDTO;



public class JwtUtil {
    
    /* 변수 */
	private static UserUtil uUtil = new UserUtil();
	private static ApiUtil aUtil = new ApiUtil();
    /*
	// 생성되기 전에 호출해서 null 되는 것으로 보임 > root-context 대신 mvc.properties > ConfigProp > ConfigUtil 사용
	@Value("#{property['jwt.key']}")
	private static String key;
    */
    private static String key = ConfigUtil.JWTkey;
	private static String issuer = ConfigUtil.JWTissuer;
	private static String headerType = ConfigUtil.JWTtype;
	private static String headerAlg = ConfigUtil.JWTalg;
	// 전체 토큰 만료 - 1시간, 액세스 토큰 만료 30분, 리프레시 토큰 만료 8시간
	private long tokenValidMilisecond = ConfigUtil.JWTexp;
    private long accessValidMilisecond = ConfigUtil.JWTacc;
    private long refreshValidMilisecond = ConfigUtil.JWTref;
    
    /* 생성자 */
    // 기본
    public JwtUtil(){}
    // 테스트용
    public JwtUtil(long tokenValidMilisecond, long accessValidMilisecond, long refreshValidMilisecond){
    	this.tokenValidMilisecond = tokenValidMilisecond;
    	this.accessValidMilisecond = accessValidMilisecond;
    	this.refreshValidMilisecond = refreshValidMilisecond;
    }
    
    /* 함수 */
  	////////// 생성 //////////
    // String 키값을 Key로
  	private Key getSigninKey(String secretKey)  throws Exception {
  		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
  		return Keys.hmacShaKeyFor(keyBytes);
  	}
  	// 랜덤 alphanumeric 생성
   	private static String generateString()  throws Exception {
         String uuid = UUID.randomUUID().toString().replaceAll("-", "");
         return uuid;
     }
    // 토큰 생성
    private String createToken(String userid, String sub, Map<String, Object> payload)  throws Exception {
        // Header 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", headerType);
        headers.put("alg", headerAlg);
        
        // Payload 설정
        payload.put("userid", userid);

        // 만료일 설정
        long validMillisecond = 0l;
        switch(sub) {
	        case "access_token":
	        	validMillisecond = accessValidMilisecond;
	        	break;
	        case "refresh_token":
	        	validMillisecond = refreshValidMilisecond;
	        	break;
	        default:
	        	validMillisecond = tokenValidMilisecond;
	        	break;
        }
        Date createdTime = new Date();
        Date expireTime = new Date(createdTime.getTime() + validMillisecond);
        
        return Jwts.builder()
                .setHeader(headers)          // Headers 설정
                .setClaims(payload)          // Claims(payload) 설정
                .setSubject(sub)             // 토큰 용도
                .setIssuer(issuer)           // 발급자
                .setIssuedAt(createdTime)    // 발급 된 시간
                .setExpiration(expireTime)   // 토큰 만료 시간
                .signWith(getSigninKey(key)) // Signatue 부분 설정    *signWith(SignatureAlgorithm.HS256, SECRET_KEY)는 deprecated 됨
                .compact();                  // 토큰 생성
    }
  	// 용도에 맞는 토큰 생성
    private String createEach(String userid, String sub)  throws Exception {
    	String token = generateString();
    	// Payload 설정
    	Map<String, Object> payload = new HashMap<>();
    	payload.put(sub, token);
    	return createToken(userid, sub, payload);
    }
    
    // 전체 토큰 생성 (whole_token도 json화)
    public String createWholeToken(UserDTO uDTO) {
    	String result = "";
    	try {
    		String access_token = createEach(uDTO.getId(), "access_token");
        	String refresh_token = createEach(uDTO.getId(), "refresh_token");
        	if(access_token!=null && !access_token.equals("") && refresh_token!=null && !refresh_token.equals("")) {
            	// Payload 설정
            	Map<String, Object> payload = new HashMap<>();
            	payload.put("access_token", access_token);
            	payload.put("refresh_token", refresh_token);
            	result = createToken(uDTO.getId(), "whole_token", payload);
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
    
    // 전체 토큰 생성 (whole_token도 json화) 후 DB 업데이트까지
    public Map<String,Object> createNupdate(UserDTO uDTO) {
    	Map<String,Object> resMap = new HashMap<String,Object>();
    	boolean result = false;
    	String whole_token = createWholeToken(uDTO);
    	if(whole_token!=null && !whole_token.equals("")) {
    		Map<String,Object> wholeMap = new HashMap<String,Object>();
    		try {
    	    	Jws<Claims> wholeClaim = getClaims(whole_token);
    	    	wholeMap = getEach(wholeClaim);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		if(!wholeMap.isEmpty()) {
    	    	Date accExp = (Date) wholeMap.get("accexp");
    	    	Timestamp accExpTs = new Timestamp(accExp.getTime());
    	    	uDTO.setAccexp(accExpTs);
    	        Date refExp = (Date) wholeMap.get("refexp");
    	        Timestamp refExpTs = new Timestamp(refExp.getTime());
    	        uDTO.setRefexp(refExpTs);
    	        boolean updone2 = uUtil.updateUser(uDTO);
    	        if(updone2) {
    	        	result = true;
    	        	resMap.put("whole_token",whole_token);
    	        }
    		}
    	}
        resMap.put("result", result);
    	return resMap;
    }
    
    // 전체 토큰 생성
    private Map<String,String> createWOwhole(UserDTO uDTO) throws Exception {
    	Map<String, String> tkMap = new HashMap<String,String>();
    	tkMap.put("access_token", createEach(uDTO.getId(), "access_token"));
    	tkMap.put("refresh_token", createEach(uDTO.getId(), "refresh_token"));
    	return tkMap;
    }
    
    // 전체 토큰 생성 후 DB 업데이트까지
    public Map<String,Object> createWOwholeNupdate(UserDTO uDTO) {
    	Map<String,Object> resMap = new HashMap<String,Object>();
    	boolean result = false;
    	Map<String,String> whole_token = new HashMap<String,String>();
    	Map<String,Object> accMap = new HashMap<String,Object>();
    	Map<String,Object> refMap = new HashMap<String,Object>();
    	try {
    		whole_token = createWOwhole(uDTO);
        	Jws<Claims> accClaim = getClaims(whole_token.get("access_token").toString());
        	accMap = getEach(accClaim);
        	Jws<Claims> refClaim = getClaims(whole_token.get("refresh_token").toString());
        	refMap = getEach(refClaim);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	if(!accMap.isEmpty() && !refMap.isEmpty()) {
    		Date accExp = (Date) accMap.get("expiration");
        	Date refExp = (Date) refMap.get("expiration");
        	Timestamp accExpTs = new Timestamp(accExp.getTime());
            Timestamp refExpTs = new Timestamp(refExp.getTime());
            uDTO.setAccexp(accExpTs);
            uDTO.setRefexp(refExpTs);
            boolean updone2 = uUtil.updateUser(uDTO);
            if(updone2) {
            	result = true;
            	resMap.put("whole_token",whole_token);
            }
    	}
        resMap.put("result", result);
    	return resMap;
    }

    ////////// 검증 //////////
    // 토큰 검증
    public boolean validateToken(String jwtToken) {
        boolean res = false;
        Key theKey = null;
        try {
        	theKey = getSigninKey(key);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        if(theKey!=null) {
            try {
                Jwts.parserBuilder()
                    .setSigningKey(theKey)
                    .build()
                    .parseClaimsJws(jwtToken);
                //System.out.println("Trust the JWT");
                res = true;
            } catch (JwtException e) {
                //System.out.println("Don't trust the JWT!");
            }
        }
        return res;
    }
    // 토큰 복호화 - 방법1
    public Map<String,String> getHeaderBody(String jwt) {
        Map<String,String> res = new HashMap<>();
        
        String[] chunks  = jwt.split("\\.");

        Base64 b64Decoder = new Base64(true);
        byte[] decodedBytesH = b64Decoder.decode(chunks[0]);
        byte[] decodedBytesP = b64Decoder.decode(chunks[1]);

        String header = new String(decodedBytesH);
        String payload = new String(decodedBytesP);
        
        res.put("header", header);
        res.put("payload", payload);

        return res;
    }
    // 토큰 복호화 - 방법2
    public Jws<Claims> getClaims(String jwt) {
    	Jws<Claims> result = null;
    	Key theKey = null;
        try {
        	theKey = getSigninKey(key);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        if(theKey!=null) {
            // secretkey로 암호화 했으면 secretkey로 복호화
            // privatekey로 암호화 했으면 publickey로 복호화
        	result = Jwts.parserBuilder()
                    .setSigningKey(theKey)
                    .build()
                    .parseClaimsJws(jwt);
        }
        return result;
    }
    // 용도에 맞는 토큰 파싱
    public Map<String,Object> getEach(Jws<Claims> claims) {
        Map<String,Object> res = new HashMap<>();
        res.put("issuer", claims.getBody().getIssuer());
        res.put("issuedAt", claims.getBody().getIssuedAt());
        res.put("expiration", claims.getBody().getExpiration());
        res.put("userid", claims.getBody().get("userid").toString());
        String sub = claims.getBody().getSubject();
        res.put("sub", sub);
        if(sub.equals("whole_token")) {
        	String token1 = claims.getBody().get("access_token").toString();
        	res.put("access_token", token1);
        	String token2 = claims.getBody().get("refresh_token").toString();
        	res.put("refresh_token", token2);
        	
        	Jws<Claims> accessClaim = getClaims(token1);
        	Map<String,Object> accessMap = getEach(accessClaim);
        	Date accexp = (Date) accessMap.get("expiration");
        	res.put("accexp", accexp);
        	Jws<Claims> refreshClaim = getClaims(token2);
        	Map<String,Object> refreshMap = getEach(refreshClaim);
        	Date refexp = (Date) refreshMap.get("expiration");
        	res.put("refexp", refexp);
        } else {
        	String token = claims.getBody().get(sub).toString();
        	res.put(sub, token);
        }
        return res;
    }
    // 발급자, 사용자 포함 토큰 검증
    public boolean validateTokenSub(String jwt, UserDTO uDTO) {
        boolean res = false;
        if(validateToken(jwt)) {
        	Jws<Claims> jws = getClaims(jwt);
        	Map<String,Object> jwtMap = getEach(jws);
        	
        	String issuerChk = jwtMap.get("issuer").toString();
        	if(issuer.equals(issuerChk)) {
        		String userChk = jwtMap.get("userid").toString();
        		if(uDTO.getId().equals(userChk)) {
                	boolean exists = uUtil.existUser(uDTO);
                	if(exists) {
                		res = true;
                	}
        		}
        	}
        }
        return res;
    }
    // 발급자, 사용자, 용도, 만료일(재발급 전 토근인지) 포함 토큰 검증
    public boolean validateTokenExp(String jwt, UserDTO uDTO) {
        boolean res = false;

        res = validateTokenSub(jwt, uDTO);
        if(res) {
        	Jws<Claims> jws = getClaims(jwt);
        	Map<String,Object> jwtMap = getEach(jws);
        	String sub = jwtMap.get("sub").toString();
        	
    		Map<String,Object> userMap = uUtil.getUser(uDTO);
    		if(userMap.get("accexp") != null) {
    			Date accessExp = null;
    			if(sub.equals("whole_token")) {
    				String access_token = jwtMap.get("access_token").toString();
	        		Map<String,Object> accessMap = getEach(getClaims(access_token));
	        		accessExp = (Date) accessMap.get("expiration");
    			} else if(sub.equals("access_token")) {
    				accessExp = (Date) jwtMap.get("expiration");
    			}
    			if(accessExp != null) {
    				Timestamp accessExpTs = new Timestamp(accessExp.getTime());
        			Timestamp accDbTs = (Timestamp) userMap.get("accexp");
        			if(!accessExpTs.equals(accDbTs)) {
            			res = false;
            		}
    			}
    		}
    		if(userMap.get("refexp") != null) {
    			Date refreshExp = null;
    			if(sub.equals("whole_token")) {
    				String refresh_token = jwtMap.get("refresh_token").toString();
	        		Map<String,Object> refreshMap = getEach(getClaims(refresh_token));
	        		refreshExp = (Date) refreshMap.get("expiration");
    			} else if(sub.equals("refresh_token")) {
    				refreshExp = (Date) jwtMap.get("expiration");
    			}
    			if(refreshExp != null) {
    				Timestamp refreshExpTs = new Timestamp(refreshExp.getTime());
        			Timestamp refDbTs = (Timestamp) userMap.get("refexp");
            		if(!refreshExpTs.equals(refDbTs)) {
            			res = false;
            		}
    			}
    		}
        }
        return res;
    }
    
	//////////Controller 역할 축소 //////////    
	// ID/PW 확인 후 JWT 발급
	public Map<String,Object> tokenpw(HttpServletRequest request) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		
	    boolean result = false;
		String msg = "";
	
		Map<String,Object> jic = aUtil.readBody(request);
		if((boolean)jic.get("result")) {
			String body = jic.get("body").toString();
			UserDTO uDTO = uUtil.getUserFromStr(body);
			if(uUtil.correctUser(uDTO)) {
				//Map<String,Object> jMap = jUtil.createNupdate(uDTO);       //whole_token까지 jwt화
				Map<String,Object> jMap = createWOwholeNupdate(uDTO);
				if((boolean)jMap.get("result")) {
					result = true;
					//jObj.put("whole_token", jMap.get("whole_token").toString());
					Map<String,String> wholeMap = (Map<String,String>)jMap.get("whole_token");
					resMap.put("token_info", new JSONObject(wholeMap));
				}
			} else {
				msg = "Wrong PW / Unregistered User";
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		resMap.put("result", result);
		if(!result) {
			resMap.put("msg", msg);
		}
		return resMap;
	}
	// Refresh Token 확인 후 재발급
	public Map<String,Object> tokenref(HttpServletRequest request) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		boolean result = false;
		String msg = "";

		// 헤더의 토큰만 체크 - body 읽지 않고
		Map<String,Object> jic = aUtil.chkHeaderToken(request, "refresh_token");
		if((boolean)jic.get("result")) {
			UserDTO uDTO = new UserDTO();
			uDTO.setId(jic.get("userid").toString());
			//Map<String,Object> jMap = jUtil.createNupdate(uDTO);       //whole_token까지 jwt화
			Map<String,Object> jMap = createWOwholeNupdate(uDTO);
			if((boolean)jMap.get("result")) {
				result = true;
				//jObj.put("whole_token", jMap.get("whole_token").toString());
				Map<String,String> wholeMap = (Map<String,String>)jMap.get("whole_token");
				resMap.put("token_info", new JSONObject(wholeMap));
			}
		} else {
			msg = jic.get("msg").toString();
		}
		
		resMap.put("result", result);
		if(!result) {
			resMap.put("msg", msg);
		}
		return resMap;
	}
	// Access Token 확인 후 응답
	public Map<String,Object> testapi(HttpServletRequest request) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		boolean result = false;
		String msg = "";

		// 헤더의 토큰까지 체크
		Map<String,Object> jic = aUtil.readReq(request,"access_token");
		if((boolean)jic.get("result")) {
			result = true;
			resMap.put("body", "correct");
		} else {
			msg = jic.get("msg").toString();
		}
		
		resMap.put("result", result);
		if(!result) {
			resMap.put("msg", msg);
		}
		return resMap;
	}
}