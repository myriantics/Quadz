package dev.lazurite.quadz.client.render;

public class QuadcopterView/* extends View implements View.Ticking*/ {
/*
    private static final ManagedShaderEffect STATIC_SHADER = ShaderEffectManager.getInstance().manage(new ResourceLocation(QuadzCommon.MOD_ID, "shaders/post/static.json"));
    private static final ManagedShaderEffect FISHEYE_SHADER = ShaderEffectManager.getInstance().manage(new ResourceLocation(QuadzCommon.MOD_ID, "shaders/post/fisheye.json"));
    private static final Uniform1f STATIC_AMOUNT = STATIC_SHADER.findUniform1f("Amount");
    private static final Uniform1f STATIC_TIMER = STATIC_SHADER.findUniform1f("Time");
    private static final Uniform1f FISHEYE_AMOUNT = FISHEYE_SHADER.findUniform1f("Amount");

    private static final int SIGNAL_DISTANCE = 1024;

    private final Quadcopter quadcopter;
    private final OnScreenDisplay osd;
    private final Clock clock;
    private Template template;

    public QuadcopterView(Quadcopter quadcopter) {
        this.quadcopter = quadcopter;
        this.osd = new OnScreenDisplay(quadcopter);
        this.clock = new Clock();
    }

    @Override
    public void tick() {
        FISHEYE_AMOUNT.set(Config.fisheyeAmount);

        var distance = getQuadcopter().distanceTo(Minecraft.getInstance().player);
        var lineOfSight = getQuadcopter().hasLineOfSight(Minecraft.getInstance().player);
        STATIC_AMOUNT.set(lineOfSight ? Mth.clamp(distance / (float) SIGNAL_DISTANCE, 0.0f, 1.0f) * 2.0f : 2.0f);
    }

    @Override
    public void onRender() {
        var options = Minecraft.getInstance().options;
        var camera = this.getCamera();

        if (options.getCameraType().isFirstPerson()) {
            if (!Config.renderCameraInCenter) {
                var template = this.getTemplate();
                float cameraX = template.metadata().get("cameraX").getAsFloat();
                float cameraY = template.metadata().get("cameraY").getAsFloat();
                camera.move(-cameraX, -cameraY, 0);
            }
        } else {
            camera.move(camera.getMaxZoom(4), 0, 0);
        }
    }

    public void onGuiRender(PoseStack poseStack, float tickDelta) {
        var firstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();

        if (firstPerson && Config.fisheyeEnabled) {
            FISHEYE_SHADER.render(tickDelta);
        }

        if (Config.osdEnabled) {
            this.osd.renderVelocity(poseStack, tickDelta);
            this.osd.renderSticks(poseStack, tickDelta);
        }

        if (firstPerson && Config.videoInterferenceEnabled) {
            STATIC_TIMER.set(clock.get());
            STATIC_SHADER.render(tickDelta);
        }
    }

    @Override
    public Vec3 getPosition(float tickDelta) {
        return VectorHelper.toVec3(Convert.toMinecraft(this.quadcopter.getPhysicsLocation(new Vector3f(), tickDelta)));
    }

    @Override
    public Quaternionf getRotation(float tickDelta) {
        return QuaternionHelper.rotateY(QuaternionHelper.rotateX(
                Convert.toMinecraft(this.quadcopter.getPhysicsRotation(new Quaternion(), tickDelta)),
                -this.quadcopter.getEntityData().get(Quadcopter.CAMERA_ANGLE)
        ), 180);
    }

    @Override
    public boolean shouldRenderTarget() {
        return (!Config.renderCameraInCenter && Config.renderFirstPerson) || !Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

    public Quadcopter getQuadcopter() {
        return this.quadcopter;
    }

    private Template getTemplate() {
        // cache the template object
        if (this.template == null) {
            this.template = TemplateLoader.getTemplateById(this.quadcopter.getTemplate()).orElse(null);
        }

        return this.template;
    }*/

}
