package net.openhft.chronicle.engine2.map;

import net.openhft.chronicle.engine2.api.*;
import net.openhft.chronicle.engine2.api.map.MapView;

import java.util.function.Supplier;

/**
 * Created by peter on 23/05/15.
 */
public class VanillaTopicPublisher<T, M> implements TopicPublisher<T, M> {
    private final Class<T> tClass;
    private final Class<M> mClass;
    private Asset asset;
    private MapView<T, M, M> underlying;

    public VanillaTopicPublisher(RequestContext context, Asset asset, Supplier<Assetted> assetted) {
        this(asset, context.type(), context.type2(), (MapView<T, M, M>) assetted.get());
    }

    VanillaTopicPublisher(Asset asset, Class<T> tClass, Class<M> mClass, MapView<T, M, M> underlying) {
        this.asset = asset;
        this.tClass = tClass;
        this.mClass = mClass;
        this.underlying = underlying;
    }

    @Override
    public void publish(T topic, M message) {
        underlying.put(topic, message);
    }

    @Override
    public Asset asset() {
        return asset;
    }

    @Override
    public MapView<T, M, M> underlying() {
        return underlying;
    }

    @Override
    public void registerTopicSubscriber(TopicSubscriber<T, M> topicSubscriber) {
        asset.subscription(true).registerTopicSubscriber(RequestContext.requestContext().bootstrap(true).type(tClass).type2(mClass), topicSubscriber);
    }
}
