package dev.scx.http.routing.x.cors.allow_origin;

public sealed interface AllowOrigin permits ListAllowOrigin, WildcardAllowOrigin {

    // todo 名字没想好
    String xxxOrigin(String origin);

}
