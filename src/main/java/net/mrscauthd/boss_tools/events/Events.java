package net.mrscauthd.boss_tools.events;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrscauthd.boss_tools.ModInnet;
import net.mrscauthd.boss_tools.entity.*;
import net.mrscauthd.boss_tools.item.RoverItemItem;
import net.mrscauthd.boss_tools.item.Tier1RocketItemItem;
import net.mrscauthd.boss_tools.item.Tier2RocketItemItem;
import net.mrscauthd.boss_tools.item.Tier3RocketItemItem;

import java.util.Map;

@Mod.EventBusSubscriber
public class Events {
    public static double counter = 1;
    public static boolean check = false;
    //Player Tick
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Entity entity = event.player;
            World world = entity.world;
            double x = entity.getPosX();
            double y = entity.getPosY();
            double z = entity.getPosZ();
            //Venus Rain
            {
                if (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus"))))) {
                    if (((PlayerEntity) entity).abilities.isCreativeMode == false && entity.isSpectator() == false) {
                        if (((world.getWorldInfo().isRaining() == (true)) && ((world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) (Math.floor(x)), (int) (Math.floor(z)))) <= ((Math.floor(y)) + 1)))) {
                            ((LivingEntity) entity).attackEntityFrom(new DamageSource("venus.acid").setDamageBypassesArmor(), (float) 1);
                        }
                    }
                }
            }
            //Player orbit Fall Teleport
            {
                if (entity.getPosY() <= 1 && !(entity.getRidingEntity() instanceof LandingGearEntity.CustomEntity)) {
                    RegistryKey<World> world2 = (world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD);
                    //Overworld
                    if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:overworld_orbit"))) {
                        {
                            Entity _ent = entity;
                            if (!_ent.world.isRemote && _ent instanceof ServerPlayerEntity) {
                                RegistryKey<World> destinationType = World.OVERWORLD;
                                ServerWorld nextWorld = _ent.getServer().getWorld(destinationType);
                                if (nextWorld != null) {
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241768_e_, 0));
                                    ((ServerPlayerEntity) _ent).teleport(nextWorld, entity.getPosX(), 450, entity.getPosZ(), _ent.rotationYaw, _ent.rotationPitch);
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) _ent).abilities));
                                    for (EffectInstance effectinstance : ((ServerPlayerEntity) _ent).getActivePotionEffects()) {
                                        ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayEntityEffectPacket(_ent.getEntityId(), effectinstance));
                                    }
                                }
                            }
                        }
                    }
                    //Moon
                    if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon_orbit"))) {
                        {
                            Entity _ent = entity;
                            if (!_ent.world.isRemote && _ent instanceof ServerPlayerEntity) {
                                RegistryKey<World> destinationType = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon"));
                                ServerWorld nextWorld = _ent.getServer().getWorld(destinationType);
                                if (nextWorld != null) {
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241768_e_, 0));
                                    ((ServerPlayerEntity) _ent).teleport(nextWorld, entity.getPosX(), 450, entity.getPosZ(), _ent.rotationYaw, _ent.rotationPitch);
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) _ent).abilities));
                                    for (EffectInstance effectinstance : ((ServerPlayerEntity) _ent).getActivePotionEffects()) {
                                        ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayEntityEffectPacket(_ent.getEntityId(), effectinstance));
                                    }
                                }
                            }
                        }
                    }
                    //Mars
                    if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars_orbit"))) {
                        {
                            Entity _ent = entity;
                            if (!_ent.world.isRemote && _ent instanceof ServerPlayerEntity) {
                                RegistryKey<World> destinationType = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars"));
                                ServerWorld nextWorld = _ent.getServer().getWorld(destinationType);
                                if (nextWorld != null) {
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241768_e_, 0));
                                    ((ServerPlayerEntity) _ent).teleport(nextWorld, entity.getPosX(), 450, entity.getPosZ(), _ent.rotationYaw, _ent.rotationPitch);
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) _ent).abilities));
                                    for (EffectInstance effectinstance : ((ServerPlayerEntity) _ent).getActivePotionEffects()) {
                                        ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayEntityEffectPacket(_ent.getEntityId(), effectinstance));
                                    }
                                }
                            }
                        }
                    }
                    //Mercury
                    if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury_orbit"))) {
                        {
                            Entity _ent = entity;
                            if (!_ent.world.isRemote && _ent instanceof ServerPlayerEntity) {
                                RegistryKey<World> destinationType = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury"));
                                ServerWorld nextWorld = _ent.getServer().getWorld(destinationType);
                                if (nextWorld != null) {
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241768_e_, 0));
                                    ((ServerPlayerEntity) _ent).teleport(nextWorld, entity.getPosX(), 450, entity.getPosZ(), _ent.rotationYaw, _ent.rotationPitch);
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) _ent).abilities));
                                    for (EffectInstance effectinstance : ((ServerPlayerEntity) _ent).getActivePotionEffects()) {
                                        ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayEntityEffectPacket(_ent.getEntityId(), effectinstance));
                                    }
                                }
                            }
                        }
                    }
                    //Venus
                    if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus_orbit"))) {
                        {
                            Entity _ent = entity;
                            if (!_ent.world.isRemote && _ent instanceof ServerPlayerEntity) {
                                RegistryKey<World> destinationType = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus"));
                                ServerWorld nextWorld = _ent.getServer().getWorld(destinationType);
                                if (nextWorld != null) {
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241768_e_, 0));
                                    ((ServerPlayerEntity) _ent).teleport(nextWorld, entity.getPosX(), 450, entity.getPosZ(), _ent.rotationYaw, _ent.rotationPitch);
                                    ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayerAbilitiesPacket(((ServerPlayerEntity) _ent).abilities));
                                    for (EffectInstance effectinstance : ((ServerPlayerEntity) _ent).getActivePotionEffects()) {
                                        ((ServerPlayerEntity) _ent).connection.sendPacket(new SPlayEntityEffectPacket(_ent.getEntityId(), effectinstance));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Lander Warning Overlay Tick
                if (check == false) {
                    counter = counter - 0.025;
                    if (counter <= 0.2) {
                        check = true;
                    }
                }
                if (check == true) {
                    counter = counter + 0.025;
                    if (counter >= 1.2) {
                        check = false;
                    }
                }
            //Other Player Ticks
        }
    }

    //RightClickonBlock Event
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity entity = event.getPlayer();
        if (event.getHand() != entity.getActiveHand()) {
            return;
        }
        double x = event.getPos().getX();
        double y = event.getPos().getY();
        double z = event.getPos().getZ();
        IWorld world = event.getWorld();
        //Carge Coal Torch Event
        {
            //Check Dim
            if (!(((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:overworld_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus")))) || ((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus_orbit"))))))))))))) {
                //Flint and Steel check
                if (((((entity instanceof LivingEntity) ? ((LivingEntity) entity).getHeldItemMainhand() : ItemStack.EMPTY).getItem() == new ItemStack(Items.FLINT_AND_STEEL, (int) (1)).getItem()) || (((entity instanceof LivingEntity) ? ((LivingEntity) entity).getHeldItemOffhand() : ItemStack.EMPTY).getItem() == new ItemStack(Items.FLINT_AND_STEEL, (int) (1)).getItem()))) {
                    if (entity.isSneaking() == false) {

                        //Block check (wall torch)
                        if ((world.getBlockState(new BlockPos((int) (Math.floor(x)), (int) (Math.floor(y)), (int) (Math.floor(z)))))
                                .getBlock() == ModInnet.WALLCOALTORCHBLOCK.get().getDefaultState().getBlock()) {

                            //Check Direction
                            Direction TD = Direction.NORTH;
                            TD = (Direction) (new Object() {
                                public Direction getDirection(BlockPos pos) {
                                    try {
                                        BlockState _bs = world.getBlockState(pos);
                                        DirectionProperty property = (DirectionProperty) _bs.getBlock().getStateContainer().getProperty("facing");
                                        if (property != null)
                                            return _bs.get(property);
                                        return Direction.getFacingFromAxisDirection(
                                                _bs.get((EnumProperty<Direction.Axis>) _bs.getBlock().getStateContainer().getProperty("axis")),
                                                Direction.AxisDirection.POSITIVE);
                                    } catch (Exception e) {
                                        return Direction.NORTH;
                                    }
                                }
                            }.getDirection(new BlockPos((int) x, (int) y, (int) z)));

                            //Set Block
                            world.setBlockState(new BlockPos((int) x, (int) y, (int) z), Blocks.WALL_TORCH.getDefaultState(), 3);

                            //Set Rotation
                            try {
                                BlockState _bs = world.getBlockState(new BlockPos((int) x, (int) y, (int) z));
                                DirectionProperty _property = (DirectionProperty) _bs.getBlock().getStateContainer().getProperty("facing");
                                if (_property != null) {
                                    world.setBlockState(new BlockPos((int) x, (int) y, (int) z), _bs.with(_property, TD), 3);
                                } else {
                                    world.setBlockState(new BlockPos((int) x, (int) y, (int) z),
                                            _bs.with((EnumProperty<Direction.Axis>) _bs.getBlock().getStateContainer().getProperty("axis"), TD.getAxis()), 3);
                                }
                            } catch (Exception e) {
                            }

                            //Sounds
                            if (world instanceof World && !world.isRemote()) {
                                ((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z),
                                        (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.flintandsteel.use")),
                                        SoundCategory.NEUTRAL, (float) 1, (float) 1);
                            } else {
                                ((World) world).playSound(x, y, z,
                                        (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.flintandsteel.use")),
                                        SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                            }
                        }

                        //Block check (stand Torch)
                        if ((world.getBlockState(new BlockPos((int) (Math.floor(x)), (int) (Math.floor(y)), (int) (Math.floor(z)))))
                                .getBlock() == ModInnet.COALTORCHBLOCK.get().getDefaultState().getBlock()) {

                            //Set Block
                            world.setBlockState(new BlockPos((int) x, (int) y, (int) z), Blocks.TORCH.getDefaultState(), 3);

                            //Sounds
                            if (world instanceof World && !world.isRemote()) {
                                ((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z),
                                        (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.flintandsteel.use")),
                                        SoundCategory.NEUTRAL, (float) 1, (float) 1);
                            } else {
                                ((World) world).playSound(x, y, z,
                                        (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.flintandsteel.use")),
                                        SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                            }
                        }
                    }
                }
            }
        }
    }

    //BlockEvent
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        IWorld world = event.getWorld();
        double x = event.getPos().getX();
        double y = event.getPos().getY();
        double z = event.getPos().getZ();
        if ((((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon")))) || (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars")))) || (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:orbit_overworld")))) || (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury")))) || (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:orbit_moon")))) || (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:orbit_mars")))) || (((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:orbit_mercury")))) || ((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus")))) || ((world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:orbit_venus")))))))))))) {
            if (((world.getBlockState(new BlockPos((int) x, (int) y, (int) z))).getBlock() == Blocks.FIRE.getDefaultState().getBlock())) {
                if (world instanceof World && !world.isRemote()) {
                    ((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z), (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1);
                } else {
                    ((World) world).playSound(x, y, z, (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                }
                world.setBlockState(new BlockPos((int) x, (int) y, (int) z), Blocks.AIR.getDefaultState(), 3);
            }
            if (((world.getBlockState(new BlockPos((int) x, (int) y, (int) z))).getBlock() == Blocks.WALL_TORCH.getDefaultState().getBlock())) {
                Direction TD = Direction.NORTH;
                TD = (Direction) (new Object() {
                    public Direction getDirection(BlockPos pos) {
                        try {
                            BlockState _bs = world.getBlockState(pos);
                            DirectionProperty property = (DirectionProperty) _bs.getBlock().getStateContainer().getProperty("facing");
                            if (property != null)
                                return _bs.get(property);
                            return Direction.getFacingFromAxisDirection(
                                    _bs.get((EnumProperty<Direction.Axis>) _bs.getBlock().getStateContainer().getProperty("axis")),
                                    Direction.AxisDirection.POSITIVE);
                        } catch (Exception e) {
                            return Direction.NORTH;
                        }
                    }
                }.getDirection(new BlockPos((int) x, (int) y, (int) z)));

                if (world instanceof World && !world.isRemote()) {
                    ((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z), (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1);
                } else {
                    ((World) world).playSound(x, y, z, (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                }

                world.setBlockState(new BlockPos((int) x, (int) y, (int) z), ModInnet.WALLCOALTORCHBLOCK.get().getDefaultState(), 3);
                try {
                    BlockState _bs = world.getBlockState(new BlockPos((int) x, (int) y, (int) z));
                    DirectionProperty _property = (DirectionProperty) _bs.getBlock().getStateContainer().getProperty("facing");
                    if (_property != null) {
                        world.setBlockState(new BlockPos((int) x, (int) y, (int) z), _bs.with(_property, TD), 3);
                    } else {
                        world.setBlockState(new BlockPos((int) x, (int) y, (int) z),
                                _bs.with((EnumProperty<Direction.Axis>) _bs.getBlock().getStateContainer().getProperty("axis"), TD.getAxis()), 3);
                    }
                } catch (Exception e) {
                }
            }
            if (((world.getBlockState(new BlockPos((int) x, (int) y, (int) z))).getBlock() == Blocks.TORCH.getDefaultState().getBlock())) {
                if (world instanceof World && !world.isRemote()) {((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z), (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1);
                } else {
                    ((World) world).playSound(x, y, z, (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                }

                world.setBlockState(new BlockPos((int) x, (int) y, (int) z), ModInnet.COALTORCHBLOCK.get().getDefaultState(), 3);
            }
            if (((world.getBlockState(new BlockPos((int) x, (int) y, (int) z))).getBlock() == Blocks.WALL_TORCH.getDefaultState().getBlock())) {
                if (world instanceof World && !world.isRemote()) {
                    ((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z), (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1);
                } else {
                    ((World) world).playSound(x, y, z, (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                }
                world.setBlockState(new BlockPos((int) x, (int) y, (int) z), ModInnet.COALTORCHBLOCK.get().getDefaultState(), 3);
            }
            if (((world.getBlockState(new BlockPos((int) x, (int) y, (int) z))).getBlock() == Blocks.CAMPFIRE.getDefaultState().getBlockState().with(CampfireBlock.LIT, false).getBlock())) {
                {
                    BlockPos _bp = new BlockPos((int) x, (int) y, (int) z);
                    BlockState _bs = Blocks.CAMPFIRE.getDefaultState();
                    BlockState _bso = world.getBlockState(_bp);
                    for (Map.Entry<Property<?>, Comparable<?>> entry : _bso.getValues().entrySet()) {
                        Property _property = _bs.getBlock().getStateContainer().getProperty(entry.getKey().getName());
                        if (_property != null && _bs.get(_property) != null)
                            try {
                                _bs = _bs.with(_property, (Comparable) entry.getValue());
                            } catch (Exception e) {
                            }
                    }
                    world.setBlockState(_bp, _bs.with(CampfireBlock.LIT, false), 3);

                    if (world instanceof World && !world.isRemote()) {
                        ((World) world).playSound(null, new BlockPos((int) x, (int) y, (int) z), (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1);
                    } else {
                        ((World) world).playSound(x, y, z, (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.extinguish_fire")), SoundCategory.NEUTRAL, (float) 1, (float) 1, false);
                    }
                }

                //world.setBlockState(new BlockPos((int) x, (int) y, (int) z), Blocks.CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, false), 3);
            }
        }
    }

    //OnEntityTick Event
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        Entity entity = event.getEntityLiving();
        World world = entity.world;
        double x = entity.getPosX();
        double y = entity.getPosY();
        double z = entity.getPosZ();
        if (Config.EntityOxygenSystem == true) {
            if ((EntityTypeTags.getCollection().getTagByID(new ResourceLocation(("forge:entities/space").toLowerCase(java.util.Locale.ENGLISH))).contains(entity.getType()))) {
                if ((((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:overworld_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury_orbit")))) || (((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus")))) || ((entity.world.getDimensionKey()) == (RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus_orbit"))))))))))))) {
                    entity.getPersistentData().putDouble("tick", ((entity.getPersistentData().getDouble("tick")) + 1));
                    if (((entity.getPersistentData().getDouble("tick")) >= 15)) {
                        world.addParticle(ParticleTypes.SMOKE, x, (y + 1), z, 0, 0, 0);
                        if (entity instanceof LivingEntity) {
                            ((LivingEntity) entity).attackEntityFrom(new DamageSource("space").setDamageBypassesArmor(), (float) 1);
                        }
                        entity.getPersistentData().putDouble("tick", 0);
                    }
                }
            }
        }
    }

    //Camera Event
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void CameraPos(EntityViewRenderEvent.CameraSetup event) {
        if (event.getInfo().getRenderViewEntity().getRidingEntity() instanceof RocketTier1Entity.CustomEntity || event.getInfo().getRenderViewEntity().getRidingEntity() instanceof RocketTier2Entity.CustomEntity || event.getInfo().getRenderViewEntity().getRidingEntity() instanceof RocketTier3Entity.CustomEntity || event.getInfo().getRenderViewEntity().getRidingEntity() instanceof LandingGearEntity.CustomEntity) {
            if (Minecraft.getInstance().gameSettings.getPointOfView().equals(PointOfView.THIRD_PERSON_FRONT)) {
                event.getInfo().movePosition(-event.getInfo().calcCameraDistance(8d), 0d, 0);
            }
            if (Minecraft.getInstance().gameSettings.getPointOfView().equals(PointOfView.THIRD_PERSON_BACK)) {
                event.getInfo().movePosition(-event.getInfo().calcCameraDistance(8d), 0d, 0);
            }
        }
    }
    //Render Player Event
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void render(RenderPlayerEvent event) {
        if (((event.getEntity().getRidingEntity()) instanceof LandingGearEntity.CustomEntity)) {
            event.getMatrixStack().scale(0f, 0f, 0f);
            // event.getRenderer();
        }
    }
    //Obfuscate Rotation Event
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupPlayerAngles(PlayerModelEvent.SetupAngles.Post event) {
        PlayerEntity player = event.getPlayer();
        PlayerModel model = event.getModelPlayer();
        //Player Rocket Sit Rotations
        {
            if (player.getRidingEntity() instanceof RocketTier1Entity.CustomEntity || player.getRidingEntity() instanceof RocketTier2Entity.CustomEntity || player.getRidingEntity() instanceof RocketTier3Entity.CustomEntity) {
                model.bipedRightLeg.rotationPointY = (float) Math.toRadians(485F);
                model.bipedLeftLeg.rotationPointY = (float) Math.toRadians(485F);
                model.bipedRightLeg.rotateAngleX = (float) Math.toRadians(0F);
                model.bipedLeftLeg.rotateAngleX = (float) Math.toRadians(0F);
                model.bipedLeftLeg.rotateAngleY = (float) Math.toRadians(3F);
                model.bipedRightLeg.rotateAngleY = (float) Math.toRadians(3F);
                // Arms
                model.bipedRightArm.rotationPointX = (float) Math.toRadians(-250F);// -200
                model.bipedLeftArm.rotationPointX = (float) Math.toRadians(250F);
                model.bipedLeftArm.rotateAngleX = (float) -0.07;
                model.bipedRightArm.rotateAngleX = (float) -0.07;
            }
        }
        //Player Hold Vehicles Rotation
        {
            if (!(player.getRidingEntity() instanceof RocketTier1Entity.CustomEntity) && !(player.getRidingEntity() instanceof RocketTier2Entity.CustomEntity) && !(player.getRidingEntity() instanceof RocketTier3Entity.CustomEntity)) {
                Item item1 = ((player instanceof LivingEntity) ? ((LivingEntity) player).getHeldItemMainhand() : ItemStack.EMPTY).getItem();
                Item item2 = ((player instanceof LivingEntity) ? ((LivingEntity) player).getHeldItemOffhand() : ItemStack.EMPTY).getItem();
                if (item1 == new ItemStack(Tier1RocketItemItem.block, (int) (1)).getItem()
                        || item1 == new ItemStack(Tier2RocketItemItem.block, (int) (1)).getItem()
                        || item1 == new ItemStack(Tier3RocketItemItem.block, (int) (1)).getItem()
                        || item1 == new ItemStack(RoverItemItem.block, (int) (1)).getItem()
                        //Off Hand
                        || item2 == new ItemStack(Tier1RocketItemItem.block, (int) (1)).getItem()
                        || item2 == new ItemStack(Tier2RocketItemItem.block, (int) (1)).getItem()
                        || item2 == new ItemStack(Tier3RocketItemItem.block, (int) (1)).getItem()
                        || item2 == new ItemStack(RoverItemItem.block, (int) (1)).getItem()) {
                    model.bipedRightArm.rotateAngleX = (float) 10;
                    model.bipedLeftArm.rotateAngleX = (float) 10;
                    model.bipedLeftArm.rotateAngleZ = (float) 0;
                    model.bipedRightArm.rotateAngleZ = (float) 0;
                    model.bipedRightArm.rotateAngleY = (float) 0;
                    model.bipedLeftArm.rotateAngleY = (float) 0;
                }
            }
        }
    }
    //Obfuscate Item Render Event
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void ItemRender(RenderItemEvent.Held event) {
        Entity player = event.getEntity();
        if (player.getRidingEntity() instanceof RocketTier1Entity.CustomEntity || player.getRidingEntity() instanceof RocketTier2Entity.CustomEntity || player.getRidingEntity() instanceof RocketTier3Entity.CustomEntity) {
            event.setCanceled(true);
        }
    }
    //TODO:Change Dimension Id in Teleport Code
    //World Tick Event
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            World world = event.world;
            RegistryKey<World> world2 = (world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD);
            if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:moon_orbit"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mars_orbit"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:mercury_orbit"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus_orbit"))
             || world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:overworld_orbit"))) {
                world.thunderingStrength = 0;
                world.rainingStrength = 0;
            }
            if (world2 == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("boss_tools:venus"))) {
                world.thunderingStrength = 0;
            }
        }
    }
    //Overlay Event
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void Overlay(RenderGameOverlayEvent event) {
        //Disable Food Overlay
        {
            if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
                PlayerEntity entity = Minecraft.getInstance().player;
                if (entity.getRidingEntity() instanceof RocketTier1Entity.CustomEntity || entity.getRidingEntity() instanceof RocketTier3Entity.CustomEntity || entity.getRidingEntity() instanceof RocketTier2Entity.CustomEntity || entity.getRidingEntity() instanceof LandingGearEntity.CustomEntity || entity.getRidingEntity() instanceof RoverEntity.CustomEntity) {
                    event.setCanceled(true);
                }
            }
        }
        //Lander Warning Overlay
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            int posX = (event.getWindow().getScaledWidth()) / 2;
            int posY = (event.getWindow().getScaledHeight()) / 2;
            PlayerEntity entity = Minecraft.getInstance().player;
            World world = entity.world;
            double x = entity.getPosX();
            double y = entity.getPosY();
            double z = entity.getPosZ();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            if ((entity.getRidingEntity()) instanceof LandingGearEntity.CustomEntity && entity.getRidingEntity().isOnGround() == false && entity.areEyesInFluid(FluidTags.WATER) == (false)) {
                RenderSystem.color4f((float) counter, (float) counter, (float) counter, (float) counter);
                // Plinken
                Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("boss_tools:textures/warning1.png"));
                Minecraft.getInstance().ingameGUI.blit(event.getMatrixStack(), 0, 0, 0, 0, event.getWindow().getScaledWidth(),
                        event.getWindow().getScaledHeight(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if ((entity.getRidingEntity()) instanceof LandingGearEntity.CustomEntity && entity.getRidingEntity().isOnGround() == false && entity.areEyesInFluid(FluidTags.WATER) == (false)) {
                double speed = Math.round(100.0 * (entity.getRidingEntity()).getMotion().getY()) / 100.0;
                double speedcheck = speed;
                Minecraft.getInstance().fontRenderer.drawString(event.getMatrixStack(), "" + speedcheck + " Speed",
                        event.getWindow().getScaledWidth() / 2 - 29, event.getWindow().getScaledHeight() / 2 / 2.3f, -3407872);
            }
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    //Other Events
}