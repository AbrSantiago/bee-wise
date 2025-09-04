import MainLayout from "../../components/layout/MainLayout";
import { motion } from "framer-motion";

export function ChallengesPage() {
  return (
    <MainLayout title="Desafíos">
      <div className="flex flex-col items-center justify-center h-[70vh] text-center">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1 }}
          className="flex flex-col items-center gap-4"
        >
          <h1 className="text-4xl font-bold text-gray-800">¡Muy Pronto!</h1>
          <p className="text-lg text-gray-500 max-w-md">
            Estamos preparando algo genial.
            Volvé más tarde para descubrir los nuevos desafíos.
          </p>
        </motion.div>
      </div>
    </MainLayout>
  );
}
