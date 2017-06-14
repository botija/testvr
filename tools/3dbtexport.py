cyl=bpy.data.meshes['Cylinder']
cyldata=bpy.data.objects['Cylinder']
cyldata.name
cyldata.location
cyldata.rotation_quaternion
cyldata.scale
cyldata.active_material.name
#cyldata.data.vertices  --> bpy.data.mesh
for v in cyl.vertices:
  #v.co
  #v.normal
  print("X=",v.co.x,", Y=", v.co.y, ", Z=", v.co.z)
 

cyl.vertex_colors[0].data[0].color.r
#ejemplo  
class EmptyUV:
  uv = (0.0, 0.0)
  def __getitem__(self, index): return self
  
class EmptyColor:
  color = (1.0, 1.0, 1.0)
  def __getitem__(self, index): return self

#ob = bpy.context.object
#uv_act = ob.data.uv_layers.active
uv_act = cyl.uv_layers.active
uv_layer = uv_act.data if uv_act is not None else EmptyUV()
color_data = cyl.vertex_colors[0].data

verts = cyl.vertices
loop_vert = {l.index: l.vertex_index for l in cyl.loops}

for face in cyl.polygons:
    for li in face.loop_indices:
        struct.pack("fff", *verts[loop_vert[li]].normal)
        struct.pack("fff", *verts[loop_vert[li]].co)
        struct.pack("ff", *uv_layer[li].uv)
		struct.pack("fff", *color_data[li].color) #*color_data[loop_vert[li]].color
#/ejemplo		

class Mesh:
  def __init__(self, model_name):
    self.mesh = bpy.data.meshes[model_name]
    self.data = bpy.data.objects[model_name]
	
  def export(self):
    uv_act = self.mesh.uv_layers.active
    uv_layer = uv_act.data if uv_act is not None else EmptyUV()
    color_data = self.mesh.vertex_colors[0].data
	
	verts = self.mesh.vertices
	loop_vert = {l.index: l.vertex_index for l in self.mesh.loops}
	
	for face in self.mesh.polygons:
		for li in face.loop_indices:
			print("Normal:", *verts[loop_vert[li]].normal)
			print("Position:", *verts[loop_vert[li]].co)
			print("UV:", *uv_layer[li].uv)
			print("Color:", *color_data[li].color) #*color_data[loop_vert[li]].color
	