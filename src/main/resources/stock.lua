---
--- Generated by Luanalysis
--- Created by snoW.
--- DateTime: 2023/9/2 22:47
---
if(redis.call("exist",KEYS[1])==1) then
    local stock=tonumber(redis.call("get",KEYS[1]));
    if(stock>0) then
        redis.call("incryby",KEYS[1],-1);
        return stock;
    end;
        return 0;
end;
