package dev.scx.http.routing.x.static_files;

import dev.scx.http.exception.NotFoundException;
import dev.scx.http.routing.RoutingContext;

import java.nio.file.Path;

import static dev.scx.http.method.HttpMethod.GET;
import static dev.scx.http.method.HttpMethod.HEAD;
import static dev.scx.http.routing.x.static_files.StaticFilesHandlerHelper.restToRelativePath;

public final class StaticFilesHandlerImpl implements StaticFilesHandler {

    private final Path root;

    public StaticFilesHandlerImpl(Path root) {
        // 转换为 绝对路径并归一化, 保证后续判断稳定
        this.root = root.toAbsolutePath().normalize();
    }

    @Override
    public void apply(RoutingContext context) throws Throwable {
        var request = context.request();
        var response = request.response();

        // 0, 只关心 GET 和 HEAD
        if (request.method() != GET && request.method() != HEAD) {
            context.next();
            return;
        }

        // 1, 获取捕获
        var rest = context.pathMatch().capture("*");

        // 2, 捕获转相对路径.
        var relativePath = restToRelativePath(rest);

        // 3, 这里 target 一定是绝对路径
        var target = root.resolve(relativePath).normalize();

        // 4, 校验 target 是否越界
        if (!target.startsWith(root)) {
            throw new NotFoundException();
        }



        response.send("'" + target.toString() + "'");

    }

}
